package code.Domain.movieRating;

import code.Domain.Movie.MovieEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name="movieRatingTable")
@Data
@Getter
@Setter
public class MovieRatingEntity {

    //    평점 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieRatingId;
    //    영화 평점
    private Integer movieRating;
    //    영화 고유번호
    @ManyToOne
    private MovieEntity movieEntity;
    //    유저 고유번호
//    private String userNo;
}
