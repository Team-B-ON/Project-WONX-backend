package io.github.bon.wonx.domain.reviews.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.reviews.Review;

public record AdminReviewDto(
    UUID id,
    UUID userId,
    UUID movieId,
    Integer rating,
    String content,
    Boolean isAnonymous,
    Boolean isDeleted,
    LocalDateTime createdAt
) {
    public static AdminReviewDto fromEntity(Review review) {
        return new AdminReviewDto(
            review.getId(),
            review.getUser().getId(),
            review.getMovie().getId(),
            review.getRating(),
            review.getContent(),
            review.getIsAnonymous(),
            review.getIsDeleted(),
            review.getCreatedAt()
        );
    }
}
