package code.Service;

import code.Domain.Movie.MovieEntity;
import code.Domain.Movie.MovieRepository;
import code.Domain.movieRating.MovieRatingEntity;
import code.Domain.movieRating.MovieRatingRepository;
import code.KobisAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {

    private final KobisAPI boa;
    private final MovieRepository movieRepository;
    private final MovieRatingRepository movieRatingRepository;


    private Sort sortByRating() {
        return Sort.by(Sort.Direction.DESC, "rating");
    }

    public void showList(HttpServletResponse response){

        Sort sort = sortByRating();

//        각각의 평점 수치를 배열에 할당, 0.0부터 5.0까지 총 51개의 숫자
        List<MovieEntity>[] arr = new List[51];

        for( double i = 50 ; i >= 0 ; i--){
            List<MovieEntity> movies = movieRepository.findAllByMovieRating(Double.parseDouble(String.format("%.1f",i*0.1)), Sort.by(Sort.Direction.DESC, "views"));
            arr[(int)(50 - i)] = movies;
        }

        for ( List<MovieEntity> e : arr){
            for ( MovieEntity m : e){
                try {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    response.getWriter().println(m.getMovieCd() + " " +m.getMovieNm() + " " +m.getViews() + "회 조회"+ "<br>");
                } catch (Exception err) {
                    err.printStackTrace();
                }
//                System.out.println(m.getRating() + " " +m.getMovieCd());
            }
        }


    }


    public void showMovie(HttpServletResponse response, String movieCd){
        MovieEntity m = movieRepository.findByMovieCd(movieCd);
        Integer nowViews = m.getViews();
        if( nowViews == null){
            nowViews = 0;
        }
        m.setViews(nowViews+= 1);
        movieRepository.save(m);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    일단 보류
//    평점 생성 메서드
//    public MovieRatingEntity movieRatingCreate(MovieEntity movieEntity, Integer rating) {
//        MovieRatingEntity movieRatingEntity = new MovieRatingEntity();
//        movieRatingEntity.setMovieRating(rating);
//        movieRatingEntity.setMovieEntity(movieEntity);
//        movieRatingEntity.setUserNo(부탁해 은수형);
//        this.movieRatingRepository.save(movieRatingEntity);
//        return movieRatingEntity;
//    }

//    평점 평균 반환 메서드
    public Double getMovieRatingAvr(MovieEntity movieEntity) {
        Double sum = 0.0;
       List<MovieRatingEntity> movieRatingEntitys =  movieEntity.getMovieRatingEntityList();
       for( MovieRatingEntity r : movieRatingEntitys){
            sum += r.getMovieRating();
       }
       Double ratingAvr = Math.floor(sum/movieRatingEntitys.size());
       movieEntity.setMovieRating(ratingAvr);
       return ratingAvr;
    }

    //    평점 설정 메서드
    public void setMovieRating(MovieEntity movieEntity, Integer rating) {
        MovieRatingEntity movieRatingEntity = new MovieRatingEntity();
        movieRatingEntity.setMovieRating(rating);
        movieEntity.getMovieRatingEntityList().add(movieRatingEntity);
        this.getMovieRatingAvr(movieEntity);
        this.movieRatingRepository.save(movieRatingEntity);
    }

}

