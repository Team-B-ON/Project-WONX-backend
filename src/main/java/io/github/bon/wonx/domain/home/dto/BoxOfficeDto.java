package io.github.bon.wonx.domain.home.dto;

import lombok.Getter;

@Getter
public class BoxOfficeDto {

  private String title; // 영화 제목
  private String posterUrl; // 영화 포스터 이미지 URL
  private int boxOfficeRank; // 박스오피스 순위

  public BoxOfficeDto(String title, String posterUrl, int boxOfficeRank) {
    this.title = title;
    this.posterUrl = posterUrl;
    this.boxOfficeRank = boxOfficeRank;
  }

}