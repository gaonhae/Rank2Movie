package code.Domain.User;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity @Builder
@Table(name="user")
@Data @AllArgsConstructor
@NoArgsConstructor
public class UserEntity
{
    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userNo; // 유저 번호

    @Column(name = "user_name", length = 12, unique = true)
    String userName; // 유저 이름
    @Column(name = "user_id", length = 28, unique = true)
    String userId; // 유저 아이디
    @Column(name = "user_password", length = 100)
    String userPassword; // 유저 패스워드
    @Column(name = "user_detail", length = 500)
    String userDetail; // 유저 설명

    @Enumerated( EnumType.STRING)
    Role role;

    @JoinColumn(name="receiver_no")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    List<UserReputationEntity> received;

    @JoinColumn(name="sender_no")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    List<UserReputationEntity> sended;

    public String getRoleKey()
    {
        return role.getKey();
    }

}
