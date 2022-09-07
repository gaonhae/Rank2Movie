package code.Service;

import code.DTO.mail.MailDto;
import code.Domain.Auth.AuthTokenEntity;
import code.Domain.Auth.AuthTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service @RequiredArgsConstructor @AllArgsConstructor
public class EmailService
{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    private static final String FROM_ADDRESS = "senderacc@naver.com"; // 새로 만든 깡통 계정
    private static final String domainName = "http://localhost:8080";

    // 인증 요청이 오면, 토큰을 생성한다음에 보내준다.
    public String authRequest(String email)
    {
        Optional<AuthTokenEntity> optional = authTokenRepository.findByEmail(email);

        // 생성과 갱신이 같은 요청에서 생기기 때문에
        // DB에 있다면 갱신, 없다면 생성한다.
        AuthTokenEntity entity = optional.orElseGet(()->new AuthTokenEntity(email));
        entity.updateTime(); // 갱신은 시간을 추가해줘야함.

        authTokenRepository.save(entity);

        MailDto mail = MailDto.builder()
                .title("환영합니다! " + email + "님!")
                .message("반갑습니다 " + email + "! \n" + domainName + "/auth/email-auth?token=" + entity.getId())
                .address(email).build();

        try{
            mailSend(mail);
        }
        catch (MailException m)
        {
            return m.getMessage();
        }

        return "success";
    }
    // 토큰을 인증해주는 단계
    public boolean tokenAuth(String tokenId) throws Exception
    {
        AuthTokenEntity entity = authTokenRepository.findById(tokenId).orElseThrow(() -> new Exception("토큰을 찾을 수 없습니다."));
        // 만료된 토큰인지 확인
        if(entity.checkExpired())
        {
            authTokenRepository.save(entity);
            throw new Exception("만료된 토큰입니다.");
        }

        entity.setAuthentic(true);
        authTokenRepository.save(entity);

        return true;
    }

    public String tokenCheck(String email)
    {
        try
        {
            AuthTokenEntity entity = authTokenRepository.findByEmail(email).orElseThrow(() -> new Exception("인증 토큰을 찾을 수 없습니다."));
            if(entity.checkExpired())
            {
                authTokenRepository.save(entity);
                throw new Exception("만료된 토큰입니다. 재인증 부탁드립니다.");
            }
            if(entity.getAuthentic())
            {
                return "success";
            }
            else
            {
                return "인증을 확인하지 못했습니다.";
            }

        }
        catch (Exception e)
        {
            return e.getMessage();
        }

    }

    public void mailSend(MailDto mailDto) throws MailException {

        // 잘못된 이메일이라면 오류가 난다.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);

    }

}
