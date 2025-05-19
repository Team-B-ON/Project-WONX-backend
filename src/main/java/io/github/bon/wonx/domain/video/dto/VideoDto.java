package io.github.bon.wonx.domain.video.dto;

import io.github.bon.wonx.domain.video.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VideoDto {

  private String title;
  private String description;
  private String posterUrl;

  // 콘텐츠 기본 정보 반환용
  public static VideoDto from(Video video) {
    return new VideoDto(
        video.getTitle(),
        video.getDescription(),
        video.getPosterUrl());
  }
}
