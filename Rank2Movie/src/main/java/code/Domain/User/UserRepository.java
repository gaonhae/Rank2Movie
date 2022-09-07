package code.Domain.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    @Query(value = "SELECT * FROM user WHERE user_id = :id", nativeQuery = true)
    UserEntity findById(@Param("id") String id);

    @Query(value="SELECT * FROM user WHERE user_no = :no", nativeQuery = true)
    UserEntity findByUserNo(@Param("no") Long no);

}
