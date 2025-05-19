package io.github.bon.wonx.domain.tmdb;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.tmdb.dto.TmdbMovieDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TmdbMovieService {
  private final MovieRepository movieRepository;

  // TMDB 응답 DTO 리스트를 받아서 Movie 엔티티로 저장
  public void saveTmdbMovies(List<TmdbMovieDto> dtos) {
    for (int i = 0; i < dtos.size(); i++) {
      TmdbMovieDto dto = dtos.get(i);

      // 중복 체크: 제목 + 개봉일 기준
      if (movieRepository.existsByTitleAndReleaseDate(dto.getTitle(), LocalDate.parse(dto.getReleaseDate()))) {
        continue;
      }

      Movie movie = Movie.builder()
          .title(dto.getTitle())
          .posterUrl("https://image.tmdb.org/t/p/w500" + dto.getPosterPath())
          .releaseDate(LocalDate.parse(dto.getReleaseDate()))
          .description(dto.getOverview())
          .boxOfficeRank(i + 1) // 순위 기반
          .viewCount(0) // 초기 조회수는 0으로 세팅
          .build();

      movieRepository.save(movie);
    }
  }
}
