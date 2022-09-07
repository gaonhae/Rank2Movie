package code.DTO.user;

import code.Domain.User.Role;
import code.Domain.User.UserEntity;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter@Setter@Builder@ToString@EqualsAndHashCode
@RequiredArgsConstructor@AllArgsConstructor
public class UpdateDto
{
    Long no;

    @Size(min=1, max=12, message = "닉네임은 2자 이상, 12자 이하로 해주세요.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    String name;

    @Size(min=7, max=28, message = "아이디는 7자 이상, 28자 이하로 해주세요.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    String id;

    @NotEmpty(message = "패스워드를 입력해주셔야 합니다.")
    String pw;

    @Size(max = 500, message = "유저 설명은 500자 이내로 해야합니다.")
    String detail;

    public void updateEntity(UserEntity entity)
    {
        entity.setUserId(id);
        entity.setUserName(name);
        entity.setUserDetail(detail);
    }

}
