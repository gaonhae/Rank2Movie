package code.DTO.user;

import code.Domain.User.Role;
import code.Domain.User.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SignupDto
{
    @Size(min=1, max=12, message = "닉네임은 2자 이상, 12자 이하로 해주세요.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    String name;

    @Size(min=7, max=28, message = "아이디는 7자 이상, 28자 이하로 해주세요.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    String id;
    @Size(min=8, max=28, message = "패스워드는 8자 이상, 28자 이하로 해주세요.")
    @NotEmpty(message = "패스워드를 입력해주세요.")
    String pw;
    String repw;

    // 유효성 검사
    public boolean pwConfirm()
    {
        return pw.equals(repw);
    }

    // 엔티티로 변환시켜줍니다.
    public UserEntity toEntity()
    {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            return new UserEntity().builder()
                    .userId(id)
                    .userPassword(passwordEncoder.encode(pw))
                    .userName(name)
                    .userDetail("반갑습니다.") // 디폴트값
                    .role(Role.USER) // 디폴트값
                    .build();
    }

}
