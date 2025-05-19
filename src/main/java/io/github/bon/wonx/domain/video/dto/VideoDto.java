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

  public static VideoDto from(Video video) {
    return new VideoDto(
        video.getTitle(),
        video.getDescription(),
        video.getPosterUrl());
  }
}
