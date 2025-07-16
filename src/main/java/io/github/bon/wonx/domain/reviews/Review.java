package io.github.bon.wonx.domain.reviews;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    @Min(1) @Max(10)
    private Integer rating;

    @Column
    private String content;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Review(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public Review(User user, Movie movie, Integer rating, String content, Boolean isAnonymous) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDeleted = false;
    }

    public static Review createReview(ReviewDto dto, Movie movie, User user) {
        // 예외 발생
        if (dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패: 댓글의 id가 없어야 합니다.");
        if (!dto.getMovieId().equals(movie.getId()))
            throw new IllegalArgumentException("댓글 생성 실패: 게시글의 id가 잘못됐습니다.");
        if (!dto.getUserId().equals(user.getId()))
            throw new IllegalArgumentException("댓글 생성 실패: 유저의 id가 잘못됐습니다.");

        return new Review(
            user,
            movie,
            dto.getRating(),
            dto.getContent(),
            dto.getIsAnonymous()
        );
    }

    public void patch(Integer rating, String content) {
        if (rating != null)
            this.rating = rating;
        if (content != null && !content.isBlank())
            this.content = content;
    }
}
