package io.github.bon.wonx.domain.movies.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

  // 장르 기반 추천
  List<Movie> findByGenresIn(List<Genre> genres);

  // 박스오피스 1위
  Optional<Movie> findTop1ByOrderByBoxOfficeRankAsc();

  // 개봉 예정작
  List<Movie> findByReleaseDateBetweenOrderByReleaseDateAsc(LocalDate start, LocalDate end);

  // 조회수 내림차순 정렬
  List<Movie> findAllByOrderByViewCountDesc(Pageable pageable);

  // TMDB 저장 중 중복 체크용
  boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);
}