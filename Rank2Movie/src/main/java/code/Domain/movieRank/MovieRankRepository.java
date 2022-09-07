package code.Domain.movieRank;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankRepository extends JpaRepository<MovieRankEntity, Long> {
}