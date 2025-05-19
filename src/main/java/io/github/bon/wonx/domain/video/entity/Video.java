package io.github.bon.wonx.domain.video.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(columnDefinition = "text")
  private String description;

  private String posterUrl;

  private double rating;

  private LocalDate releaseDate;

  private Integer boxOfficeRank;

  private int viewCount;
}
