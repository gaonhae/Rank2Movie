package code.Controller;


import code.KobisAPI;
import code.NaverAPI;
import code.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final KobisAPI kobis;

    @RequestMapping(value = "/{movieCd}", produces = "application/text; charset=UTF-8")
    public String movie(HttpServletResponse response, @PathVariable("movieCd") String movieCd){
        movieService.showMovie(response, movieCd);
        return "movie/list";
    }

    @RequestMapping(value = "/movieAPI/{page}", produces = "application/text; charset=UTF-8")
    public String getMovieList(@PathVariable("page") String page){
        kobis.getMovieList(Integer.parseInt(page));
        return "movie/list";
    }

    @RequestMapping(value = "/list", produces = "application/text; charset=UTF-8")
    public String showMovie(HttpServletResponse response){
        movieService.showList(response);
        return "movie/list";
    }

    @RequestMapping("/layout")
    public String test(){
        return "movie/list";
    }
}
