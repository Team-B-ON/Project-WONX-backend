package io.github.bon.wonx.domain.home.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HotMovieDto {

  private UUID id;
  private String title; // 영화 제목
  private String posterUrl; // 포스터 이미지
  private int viewCount; // 조회수

  // Movie 엔티티 → DTO 변환 메서드
  public static HotMovieDto from(Movie movie) {
    return new HotMovieDto(
        movie.getId(),
        movie.getTitle(),
        movie.getPosterUrl(),
        movie.getViewCount() != null ? movie.getViewCount() : 0);
  }
}