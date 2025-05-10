package io.github.bon.wonx.domain.video.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoxOfficeResponse {
  private String title;
  private String overview;
  private String posterPath;
  private String releaseDate;
  private String mediaType;
}
