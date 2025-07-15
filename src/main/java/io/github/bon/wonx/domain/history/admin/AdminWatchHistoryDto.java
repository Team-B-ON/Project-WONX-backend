package io.github.bon.wonx.domain.history.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.history.WatchHistory;

public record AdminWatchHistoryDto(
    UUID id,
    UUID userId,
    UUID videoId,
    LocalDateTime watchedAt,
    Integer lastPosition,
    Integer watchedSeconds,
    LocalDateTime updatedAt,
    Boolean isCompleted
) {
    public static AdminWatchHistoryDto fromEntity(WatchHistory history) {
        return new AdminWatchHistoryDto(
            history.getId(),
            history.getUser().getId(),
            history.getMovie().getId(),
            history.getWatchedAt(),
            history.getLastPosition(),
            history.getWatchedSeconds(),
            history.getUpdatedAt(),
            history.getIsCompleted()
        );
    }
}
