package code.Domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReputationRepository extends JpaRepository<UserReputationEntity, Long>
{
    @Query(value="SELECT * FROM user_reputation WHERE receiver_no=:recv_no and sender_no=:send_no", nativeQuery = true)
    public Optional<UserReputationEntity> findByReceiverAndSender(@Param("recv_no") Long recv_no, @Param("send_no") Long send_no);

    @Query(value="SELECT * FROM user_reputation WHERE receiver_no=:recv_no", nativeQuery = true)
    public List<UserReputationEntity> findByReceiverNo(@Param("recv_no") Long recv_no);

}
