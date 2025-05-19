package io.github.bon.wonx.domain.reviews;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
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
        if (dto.getMovie().getId() != movie.getId())
            throw new IllegalArgumentException("댓글 생성 실패: 게시글의 id가 잘못됐습니다.");
        if (dto.getUser().getId() != user.getId())
            throw new IllegalArgumentException("댓글 생성 실패: 유저의 id가 잘못됐습니다.");

        return new Review(
            dto.getId(),
            user,
            movie,
            dto.getRating(),
            dto.getContent(),
            dto.getIsAnonymous(),
            dto.getIsDeleted(),
            dto.getCreatedAt()
        );
    }

    public void patch(ReviewDto dto) {
        // 예외 발생
        if (this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패: 잘못된 id가 입력됐습니다.");
            
        if (dto.getContent() != null)
            this.content = dto.getContent();
    }
}
