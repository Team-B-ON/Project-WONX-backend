package io.github.bon.wonx.domain.video.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
public class Video {

  @Id
  private UUID id; // 콘텐츠 고유 식별자

  private String title; // 콘텐츠 제목
  private String description; // 콘텐츠 설명
  private Float rating; // 콘텐츠 평점
  private Integer durationMinutes; // 영상 길이
  private LocalDate releaseDate; // 출시일
  private String ageRating; // 시청 등급
  private String backgroundVideoUrl; // 배너 등에 쓰일 영상 배경 URL
  private String thumbnailUrl; // 썸네일 이미지 URL
  private Integer viewCount; // 콘텐츠 조회수
}
