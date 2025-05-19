package io.github.bon.wonx.domain.movies.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videos")
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Movie {
  @Id
  @GeneratedValue
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column
  private Float rating;

  @Column(name = "duration_minutes")
  private Integer durationMinutes;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "poster_url")
  private String posterUrl;

  @Column(name = "age_rating")
  private String ageRating;

  @Column(name = "age_rating_reason")
  private String ageRatingReason;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "video_genre", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private List<Genre> genres;

  // @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval =
  // true, fetch = FetchType.LAZY)
  // private List<MoviePerson> moviePersons = new ArrayList<>();
}