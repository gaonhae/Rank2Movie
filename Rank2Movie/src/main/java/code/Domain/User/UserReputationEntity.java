package code.Domain.User;

import code.Domain.User.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Entity @Builder
@Table(name = "user_reputation")
@Data @AllArgsConstructor @RequiredArgsConstructor
public class UserReputationEntity
{
    public UserReputationEntity(UserEntity recv, UserEntity send)
    {
        sender = send;
        receiver = recv;
        reputation = 0;
    }

    @Column(name="user_reputation_no")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long UserLikeNo;

    @JoinColumn(name = "sender_no")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity sender;

    @JoinColumn(name = "receiver_no")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity receiver;

    @Column(name = "reputation")
    int reputation; // 좋아요 : 1, 의견철회 : 0, 싫어요 : -1

}
