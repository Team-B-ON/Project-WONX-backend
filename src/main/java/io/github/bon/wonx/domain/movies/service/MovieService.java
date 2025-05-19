package io.github.bon.wonx.domain.movies.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {
  private final MovieRepository movieRepository;

  // 영화 상세 정보 조회
  public MovieDto details(UUID id) {
    Movie movie = movieRepository.findById(id).orElse(null);
    return MovieDto.from(movie);
  }

  // 함께 시청된 콘텐츠 조회
  public List<MovieDto> relatedContents(UUID id) {
    Movie movie = movieRepository.findById(id).orElse(null);
    if (movie == null || movie.getGenres().isEmpty())
      return List.of();

    List<Genre> genres = movie.getGenres();
    List<Movie> related = movieRepository.findByGenresIn(genres);

    return related.stream()
        .filter(m -> !m.getId().equals(id))
        .map(MovieDto::from)
        .toList();
  }

  // 메인 배너
  public MovieDto getBannerMovie() {
    return movieRepository.findTop1ByOrderByBoxOfficeRankAsc()
        .map(MovieDto::from)
        .orElseThrow(() -> new RuntimeException("해당 영화를 찾을 수 없습니다."));
  }

  // 개봉 예정작
  public List<MovieDto> getUpcomingMovies() {
    LocalDate today = LocalDate.now();
    LocalDate weekLater = today.plusDays(7);

    return movieRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater)
        .stream()
        .map(MovieDto::from)
        .toList();
  }

  // 조회수 기반 인기 콘텐츠
  public List<HotMovieDto> getHotMovies(int count) {
    Pageable pageable = PageRequest.of(0, count);

    return movieRepository.findAllByOrderByViewCountDesc(pageable)
        .stream()
        .map(HotMovieDto::from)
        .toList();
  }
}