package io.github.bon.wonx.domain.search.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MovieSearchDto {

  private UUID id;
  private String title;
  private String posterUrl;

  public static MovieSearchDto from(Movie movie) {
    return MovieSearchDto.builder()
        .id(movie.getId())
        .title(movie.getTitle())
        .posterUrl(movie.getPosterUrl())
        .build();
  }
}