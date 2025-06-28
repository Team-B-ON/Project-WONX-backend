package io.github.bon.wonx.domain.home.dto;

import lombok.Getter;

@Getter
public class RecommendDto {

  private String title;
  private String posterUrl;
  private Integer boxOfficeRank;

  public RecommendDto(String title, String posterUrl, Integer boxOfficeRank) {
    this.title = title;
    this.posterUrl = posterUrl;
    this.boxOfficeRank = boxOfficeRank;
  }
}