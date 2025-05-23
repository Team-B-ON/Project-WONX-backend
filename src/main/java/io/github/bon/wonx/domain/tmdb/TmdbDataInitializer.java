package io.github.bon.wonx.domain.tmdb;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.github.bon.wonx.domain.tmdb.dto.TmdbMovieDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TmdbDataInitializer {

  private final TmdbClient tmdbClient;
  private final TmdbMovieService tmdbMovieService;

  @PostConstruct
  public void init() {
    List<TmdbMovieDto> movies = tmdbClient.fetchBoxOfficeMovies();
    tmdbMovieService.saveTmdbMovies(movies);
    System.out.println("[TMDB 초기화] 영화 " + movies.size() + "개 저장됨!");
  }
}
