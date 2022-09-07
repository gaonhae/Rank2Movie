package code.Domain.Auth;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="auth_token")
@Getter @Setter @Builder
@ToString @EqualsAndHashCode
@RequiredArgsConstructor @AllArgsConstructor
public class AuthTokenEntity
{
    private static final long expiryDate = 5L; // 만료 기한 고정값

    @Id @Column(name="id", length = 50)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    String id; // 토큰 기본 키

    @Column(name="email")
    String email; // 어떤 메일 주소를 대상으로 하는가?

    @Column(name="authentic")
    Boolean authentic; // 인증 되었는가?
    @Column(name="expired")
    Boolean expired; // 만료되었는가?
    @Column(name="expiration_date")
    LocalDateTime expirationDate; // 만료 기한

    public AuthTokenEntity(String emailAddress)
    {
        // 새로운 토큰 생성
        email = emailAddress;
        authentic = false;
        expired = false;
        expirationDate = LocalDateTime.now().plusMinutes(expiryDate);
    }

    public void updateTime()
    {
        expired = false;
        expirationDate = LocalDateTime.now().plusMinutes(expiryDate);
    }

    public boolean checkExpired()
    {
        if(LocalDateTime.now().isAfter(getExpirationDate()))
        {
            expired = true;
            return true;
        }
        else
        {
            expired = false;
            return false;
        }
    }

}
