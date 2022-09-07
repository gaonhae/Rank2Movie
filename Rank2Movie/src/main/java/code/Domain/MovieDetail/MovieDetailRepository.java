package code.Domain.MovieDetail;

import code.Domain.Movie.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDetailRepository extends JpaRepository<MovieDetailEntity, Long> {
}
