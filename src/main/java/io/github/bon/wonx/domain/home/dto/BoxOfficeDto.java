package io.github.bon.wonx.domain.home.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoxOfficeDto {

  private UUID movieId;      // 영화 ID
  private String title;      // 영화 제목
  private String posterUrl;  // 영화 포스터 이미지 URL
  private Long viewCount;    // 시청 횟수 (boxOffice 순위 기준)
}
