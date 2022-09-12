package code.Domain.movieRating;


import code.Domain.Movie.MovieEntity;
import code.Domain.movieRank.MovieRankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRatingEntity, Long> {
}