package io.github.bon.wonx.domain.home.dto;

import lombok.Getter;

@Getter
public class BoxOfficeDto {

  private String title;
  private String posterUrl;
  private int boxOfficeRank;

  public BoxOfficeDto(String title, String posterUrl, int boxOfficeRank) {
    this.title = title;
    this.posterUrl = posterUrl;
    this.boxOfficeRank = boxOfficeRank;
  }

}
