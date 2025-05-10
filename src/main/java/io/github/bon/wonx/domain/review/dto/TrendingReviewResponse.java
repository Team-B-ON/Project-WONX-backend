package io.github.bon.wonx.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class TrendingReviewResponse {
  private Long reviewId;
  private String content;
  private int viewCount;
  private String createdAt;

  private Long videoId; // 어떤 콘텐츠에 대한 리뷰인지
  private Long userId; // 누가 작성했는지
}
