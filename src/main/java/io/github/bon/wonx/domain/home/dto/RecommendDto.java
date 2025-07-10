package io.github.bon.wonx.domain.home.dto;

import java.util.UUID;
import io.github.bon.wonx.domain.movies.entity.Movie;
import lombok.Getter;

// 추천 연동 데이터 추가
@Getter
public class RecommendDto {
  private UUID id;
  private String title;
  private String posterUrl;
  private Integer boxOfficeRank;
  private String description;

  public RecommendDto(Movie movie) {
    this.id = movie.getId();
    this.title = movie.getTitle();
    this.posterUrl = movie.getPosterUrl();
    this.boxOfficeRank = movie.getBoxOfficeRank();
    this.description = movie.getDescription();
  }
}
