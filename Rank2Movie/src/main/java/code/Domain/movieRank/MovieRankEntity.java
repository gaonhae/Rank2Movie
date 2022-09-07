package code.Domain.movieRank;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="movieRankTable")
@Data
@Getter
@Setter
public class MovieRankEntity {

    public MovieRankEntity(){
        this.movieRank = "순위 없음";
        this.movieCd = "정보 없음";
    }

//    영화 순위 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieRankId;
    //    영화 순위
    private String movieRank;
    //    영화 고유번호
    private String movieCd;
}
