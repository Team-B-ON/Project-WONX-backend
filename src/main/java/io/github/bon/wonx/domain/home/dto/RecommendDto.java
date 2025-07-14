package io.github.bon.wonx.domain.home.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class RecommendDto {

  private UUID id;
  private String title;
  private String posterUrl;
  private Integer boxOfficeRank;

  public RecommendDto(UUID id, String title, String posterUrl, Integer boxOfficeRank) {
    this.id = id;
    this.title = title;
    this.posterUrl = posterUrl;
    this.boxOfficeRank = boxOfficeRank;
  }
}