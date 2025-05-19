package io.github.bon.wonx.domain.video.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 조회수 기반 인기 콘텐츠
@Getter
@AllArgsConstructor
public class HotVideoDto {

  private String title; // 콘텐츠 제목
  private String posterUrl; // 포스터 이미지 URL
  private int viewCount; // 조회수
}
