package code.Controller;

import code.Service.EmailService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    EmailService emailService;

    // 이메일 인증
    @PostMapping("/token-request")
    @ResponseBody
    public String emailAuth(@Param("email") String email) {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) return "유효한 이메일을 입력해주세요.";

        return emailService.authRequest(email);
    }

    // 이메일 인증 페이지
    @GetMapping("/email-auth")
    @ResponseBody
    public String authlogig(@Param("token") String token) {
        try {
            emailService.tokenAuth(token);
            return "인증 성공! 회원가입하시면 됩니다.";

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping("/auth-check")
    @ResponseBody
    public String authCheck(@Param("email") String email)
    {
        return emailService.tokenCheck(email);
    }

}
