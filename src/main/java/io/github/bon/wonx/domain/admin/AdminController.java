package io.github.bon.wonx.domain.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.bon.wonx.domain.tmdb.TmdbClient;
import io.github.bon.wonx.domain.tmdb.TmdbMovieService;
import io.github.bon.wonx.domain.tmdb.dto.TmdbMovieDto;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final TmdbClient tmdbClient;
  private final TmdbMovieService tmdbMovieService;

  // tmdb에서 db로 데이터 가져옴
  @GetMapping("/sync-tmdb")
  public ResponseEntity<String> syncTmdb() {
    List<TmdbMovieDto> movies = tmdbClient.fetchBoxOfficeMovies();
    tmdbMovieService.saveTmdbMovies(movies);
    return ResponseEntity.ok("TMDB 동기화 완료");
  }

}
