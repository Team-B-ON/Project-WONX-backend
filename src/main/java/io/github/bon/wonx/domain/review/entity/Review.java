package io.github.bon.wonx.domain.review.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long videoId; // 어떤 콘텐츠에 대한 리뷰인지
  private Long userId; // 누가 작성했는지
  private String content; // 리뷰 내용
  private int likeCount; // 좋아요 수 -> 리뷰 좋아요 기능 있나요?
  private LocalDateTime createdAt; // 작성 시간
  private int viewCount; // 조회수를 기록하기 위함

  public Review(Long videoId, Long userId, String content) {
    this.videoId = videoId;
    this.userId = userId;
    this.content = content;
    this.likeCount = 0;
    this.createdAt = LocalDateTime.now();
  }
}
