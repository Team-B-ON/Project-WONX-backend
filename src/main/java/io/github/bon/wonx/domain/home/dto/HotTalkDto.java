package io.github.bon.wonx.domain.home.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class HotTalkDto {
  private UUID movieId;
  private String movieTitle; // 영화 제목
  private String posterUrl; // 영화 포스터
  private String talkContent; // 리뷰 내용
  private int viewCount; // 조회수
  private LocalDateTime createdAt; // 작성 시간

  public HotTalkDto(UUID movieId, String movieTitle, String posterUrl, String talkContent, int viewCount, LocalDateTime createdAt) {
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.posterUrl = posterUrl;
    this.talkContent = talkContent;
    this.viewCount = viewCount;
    this.createdAt = createdAt;
  }
}