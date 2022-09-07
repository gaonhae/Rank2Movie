package code.Domain.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long>
{
    @Query(value="SELECT * FROM auth_token WHERE id=:id", nativeQuery = true)
    public Optional<AuthTokenEntity> findById(@Param("id")String id);

    @Query(value="SELECT * FROM auth_token WHERE email=:email", nativeQuery = true)
    public Optional<AuthTokenEntity> findByEmail(@Param("email")String email);

}
