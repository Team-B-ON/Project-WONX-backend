package io.github.bon.wonx.domain.movies.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Like;

public record AdminLikeDto(
    UUID id,
    UUID userId,
    UUID movieId,
    LocalDateTime createdAt
) {
    public static AdminLikeDto fromEntity(Like like) {
        return new AdminLikeDto(
            like.getId(),
            like.getUser().getId(),
            like.getMovie().getId(),
            like.getCreatedAt()
        );
    }
}
