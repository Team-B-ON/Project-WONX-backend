package io.github.bon.wonx.domain.home.dto;

import lombok.Getter;

@Getter
public class BoxOfficeDto {

  private String id; // 영화 고유값
  private String title; // 영화 제목
  private String posterUrl; // 영화 포스터 이미지 URL
  private int boxOfficeRank; // 박스오피스 순위

  public BoxOfficeDto(String id, String title, String posterUrl, int boxOfficeRank) {
    this.id = id;
    this.title = title;
    this.posterUrl = posterUrl;
    this.boxOfficeRank = boxOfficeRank;
  }

}