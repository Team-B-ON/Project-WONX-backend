package io.github.bon.wonx.domain.movies.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {
    private UUID userId;
    private UUID movieId;
    private boolean isBookmarked;

    public static BookmarkDto from(Bookmark bookmark) {
        return new BookmarkDto(
            bookmark.getUser().getId(),
            bookmark.getMovie().getId(),
            true
        );
    }

    public static BookmarkDto notBookmarked(UUID userId, UUID movieId) {
        return new BookmarkDto(userId, movieId, false);
    }
}
