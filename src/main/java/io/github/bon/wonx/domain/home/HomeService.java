package io.github.bon.wonx.domain.home;

import java.util.List;
import org.springframework.stereotype.Service;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.service.MovieService;
import lombok.RequiredArgsConstructor;

// tmdb에서 받은 영화 데이터를 db에 저장하는 기능
@Service
@RequiredArgsConstructor
public class HomeService {
  private final MovieService movieService;

  public MovieDto getBannerMovie() {
    return movieService.getBannerMovie();
  }

  public List<MovieDto> getUpcomingMovies() {
    return movieService.getUpcomingMovies();
  }

  public List<HotMovieDto> getHotMovies(int count) {
    return movieService.getHotMovies(count);
  }
}
