package code.Domain.MovieDetail;


import code.Domain.Movie.MovieEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="movieDetailTable")
@Data
@Getter
@Setter
public class MovieDetailEntity {

    public MovieDetailEntity(){
        this.showTm = "정보 없음";
        this.watchGradeNm = "정보 없음";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieDetailId;
    //    심의등급
    private String watchGradeNm;
    //    상영시간
    private String showTm;
}
