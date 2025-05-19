package io.github.bon.wonx.domain.movies.dto;

import java.time.LocalDate;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
  private UUID id;
  private String title;
  private String description;
  private Float rating;
  private Integer durationMinutes;
  private LocalDate releaseDate;
  private String ageRating;
  private boolean isBookmarked;
  private boolean isLiked;
  private String posterUrl; // 추가
  private Integer viewCount; // 추가

  public void setIsBookmarked(boolean isBookmarked) {
    this.isBookmarked = isBookmarked;
  }

  public void setIsLiked(boolean isLiked) {
    this.isLiked = isLiked;
  }

  public static MovieDto from(Movie movie) {
    return new MovieDto(
        movie.getId(),
        movie.getTitle(),
        movie.getDescription(),
        movie.getRating(),
        movie.getDurationMinutes(),
        movie.getReleaseDate(),
        movie.getAgeRating(),
        false,
        false, movie.getPosterUrl(), // 추가
        movie.getViewCount()); // 추가

  }
}