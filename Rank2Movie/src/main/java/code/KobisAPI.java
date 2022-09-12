package code;

import code.Domain.Movie.MovieEntity;
import code.Domain.Movie.MovieRepository;
import code.Domain.MovieDetail.MovieDetailEntity;
import code.Domain.MovieDetail.MovieDetailRepository;
import code.Domain.movieRank.MovieRankEntity;
import code.Domain.movieRank.MovieRankRepository;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Component
@RequiredArgsConstructor
public class KobisAPI {

    //API에서 영화 목록 가져올 때 이미 가져온 페이지는 가져오지 않게
    static Integer savedPage = 0;

    private final MovieRepository movieRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final MovieRankRepository movieRankRepository;
    //API 키 변수로 선언
    final String KEY = "b6bab379f60865cc96df750bc299b0db";
    //API 객체 생성
    KobisOpenAPIRestService kobis = new KobisOpenAPIRestService(KEY);

    
    //영화 리스트 가져와 DB에 저장
    public String getMovieList(int page){

        int pageCount = 1 + savedPage;
        String movieAPIList = "";
        JSONObject movieList = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        try{
            //몇 페이지만큼의 데이터를 가져올 것인지 설정
            while (pageCount <= page + savedPage) {
                //영화 목록 한 페이지 전체 가져오기기
                String[] tmp = new String[1];
                movieAPIList = kobis.getMovieList(true, Integer.toString(pageCount), "100", "", "", "2010", "", "", "", "", tmp);
                Object obj = jsonParser.parse(movieAPIList);
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject movieListResult = (JSONObject) jsonObject.get("movieListResult");
                System.out.println(movieListResult);

                JSONArray parsed_Boxoffice = (JSONArray) movieListResult.get("movieList");
                System.out.println(parsed_Boxoffice);
                for (int i = 0; i < parsed_Boxoffice.size(); i++) {
                    //해당 페이지 속 각각의 영화의 데이터를 movie에 대입
                    JSONObject movie = (JSONObject) parsed_Boxoffice.get(i);
                        try {
                            MovieEntity dailyMovie = new MovieEntity();
                            dailyMovie.setMovieNm(movie.get("movieNm").toString());
                            dailyMovie.setMovieCd(movie.get("movieCd").toString());
                            dailyMovie.setMovieNmEn(movie.get("movieNmEn").toString());
                            dailyMovie.setOpenDt(movie.get("openDt").toString());
                            dailyMovie.setCompanyNm(movie.get("companys").toString());
                            dailyMovie.setDirectors(movie.get("directors").toString());
                            dailyMovie.setGenreAlt(movie.get("genreAlt").toString());
                            dailyMovie.setPrdtStatNm(movie.get("prdtStatNm").toString());
                            dailyMovie.setRepNationNm(movie.get("repNationNm").toString());
                            movieList.put(dailyMovie.getMovieCd() , dailyMovie.getMovieNm());
                            movieRepository.save(dailyMovie);
                            getMovieDetail(movie.get("movieCd").toString());
                        } catch (NullPointerException e) {
                            System.out.println(e);
                        }
                    }
                pageCount++;
                }
            savedPage = pageCount;
        }catch(Exception e) {
            System.out.println(e);
        }
        return movieList.toString();
    }



    //개별 영화 상세 데이터 가져와 DB에 저장
    public String getMovieDetail(String movieCode) {
        JSONObject movieDetail = new JSONObject();
        MovieEntity movieEntity = movieRepository.findByMovieCd(movieCode);
        MovieDetailEntity movieDetailEntity = movieEntity.getMovieDetailEntity();
        try {
//            파싱 준비과정
            JSONParser jsonParser = new JSONParser();
            String dailyMovies = kobis.getMovieInfo(true, movieCode);
            Object obj = jsonParser.parse(dailyMovies);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject movieDetailResult = (JSONObject) jsonObject.get("movieInfoResult");
            movieDetail = (JSONObject) movieDetailResult.get("movieInfo");
//            파싱
            movieDetailEntity.setShowTm(movieDetail.get("showTm").toString());
            JSONArray audit = (JSONArray) movieDetail.get("audits");
            JSONObject tmp = (JSONObject) audit.get(0);
            movieDetailEntity.setWatchGradeNm(tmp.get("watchGradeNm").toString());
//            DB에 저장
            movieDetailRepository.save(movieDetailEntity);
            movieRepository.save(movieEntity);
        } catch (Exception e) {
            System.out.println(e);
        }
        return movieDetail.toString();
    }





    //일별 박스오피스 데이터 가져오기
    public void getDailyMovie() {
        try {
            JSONParser jsonParser = new JSONParser();

            //전날 데이터 가져오기 (당일은 불가)
            LocalDateTime time = LocalDateTime.now().minusDays(1);
            String targetDt = time.format(DateTimeFormatter.ofPattern("yyyMMdd"));

            String dailyMovies = kobis.getDailyBoxOffice(true, targetDt, "10", "", "", "");
            Object obj = jsonParser.parse(dailyMovies);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject dailyMovieResult = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray parsed_dailyMovie = (JSONArray) dailyMovieResult.get("dailyBoxOfficeList");

            for (int i = 0; i < parsed_dailyMovie.size(); i++) {
                //해당 페이지 속 각각의 영화의 데이터를 movie에 대입
                JSONObject parsed_movie = (JSONObject) parsed_dailyMovie.get(i);
                try {
//                    해당 영화 DB에서 가져오기
                    MovieEntity movieEntity = movieRepository.findByMovieCd(parsed_movie.get("movieCd").toString());
                    MovieRankEntity movieRankEntity = movieEntity.getMovieRankEntity();

                    movieRankEntity.setMovieRank(parsed_movie.get("rank").toString());
                    movieRankEntity.setMovieCd(movieEntity.getMovieCd());
                    movieRankRepository.save(movieRankEntity);
                    movieRepository.save(movieEntity);
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }





}
