package io.github.bon.wonx.domain.movies.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {
    private UUID id;
    private UUID userId;
    private UUID movieId;
    private LocalDateTime createdAt;
    private boolean isBookmarked;

    public static BookmarkDto from(Bookmark bookmark) {
        return new BookmarkDto(
                bookmark.getId(),
                bookmark.getUser().getId(),
                bookmark.getMovie().getId(),
                bookmark.getCreatedAt(),
                true);
    }

    public static BookmarkDto notBookmarked(UUID userId, UUID movieId) {
        return new BookmarkDto(
                null,
                userId,
                movieId,
                null,
                false);
    }
}
