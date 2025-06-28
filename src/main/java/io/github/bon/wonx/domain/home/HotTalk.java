package io.github.bon.wonx.domain.home;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HotTalk {
  @Id
  @GeneratedValue
  // 기본 식별자
  private UUID id;

  // 리뷰 내용
  private String content;
  // 조회수
  private int viewCount;
  // 작성일시
  private LocalDateTime createdAt;

  // 연결된 영화 정보 (제목, 포스터 등 꺼내기 위해 사용)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id")
  private Movie movie;
}