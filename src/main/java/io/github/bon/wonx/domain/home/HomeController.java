package io.github.bon.wonx.domain.home;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.home.dto.HotTalkDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
  private final HomeService homeService;

  @GetMapping("/banner")
  public ResponseEntity<MovieDto> getMainBanner() {
    return ResponseEntity.ok(homeService.getBannerMovie());
  }

  @GetMapping("/upcoming")
  public ResponseEntity<List<MovieDto>> getUpcomingMovies() {
    return ResponseEntity.ok(homeService.getUpcomingMovies());
  }

  @GetMapping("/hot-movies")
  public ResponseEntity<List<HotMovieDto>> getHotMovies(@RequestParam(defaultValue = "5") int count) {
    return ResponseEntity.ok(homeService.getHotMovies(count));
  }

  @GetMapping("/hot-talks")
  public ResponseEntity<List<HotTalkDto>> getHotTalks() {
    return ResponseEntity.ok(homeService.getHotTalks());
  }
}
