package io.github.bon.wonx.domain.movies.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

  // 박스오피스 순위 기준으로 상위 10개 영화 조회
  List<Movie> findTop10ByBoxOfficeRankIsNotNullOrderByBoxOfficeRankAsc();

  // 추천 장르 중 이미 좋아요한 영화 제외
  List<Movie> findDistinctByGenresInAndIdNotIn(List<Genre> genres, List<UUID> excludeIds);

  // 추천 콘텐츠 정렬시 조회수가 높은 영화순
  List<Movie> findDistinctByGenresInAndIdNotInOrderByViewCountDesc(List<Genre> genres, List<UUID> excludeIds);

  // 검색시 이름에 키워드 포함
  List<Movie> findByTitleContainingIgnoreCase(String keyword);

  // 연관 검색어 추천용 전체 영화 제목 가져오기
  @Query("SELECT m.title FROM Movie m")
  List<String> findAllTitles();

}