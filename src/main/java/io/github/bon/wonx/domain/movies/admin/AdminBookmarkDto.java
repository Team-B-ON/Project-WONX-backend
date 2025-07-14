package io.github.bon.wonx.domain.movies.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Bookmark;

public record AdminBookmarkDto(
    UUID id,
    UUID userId,
    UUID movieId,
    LocalDateTime createdAt
) {
    public static AdminBookmarkDto fromEntity(Bookmark bookmark) {
        return new AdminBookmarkDto(
            bookmark.getId(),
            bookmark.getUser().getId(),
            bookmark.getMovie().getId(),
            bookmark.getCreatedAt()
        );
    }
}
