package io.github.bon.wonx.domain.movies.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.github.bon.wonx.domain.reviews.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videos")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "required_plan", nullable = false)
    private MovieLevel requiredPlan = MovieLevel.BASIC;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "video_genre", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MoviePerson> moviePersons = new ArrayList<>();

    // 박스오피스 순위 정렬
    @Column(name = "box_office_rank")
    private Integer boxOfficeRank;

    // 인기 콘텐츠 정렬
    @Column(name = "view_count")
    private Integer viewCount;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
