// WatchHistoryDto.java
package io.github.bon.wonx.domain.history;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchHistoryDto {

  private UUID movieId;
  private String title;
  private String posterUrl;
  private int lastPosition;
  private int watchedSeconds;
  private boolean isCompleted;

  public static WatchHistoryDto from(WatchHistory entity) {
    return new WatchHistoryDto(
        entity.getMovie().getId(),
        entity.getMovie().getTitle(),
        entity.getMovie().getPosterUrl(),
        entity.getLastPosition() != null ? entity.getLastPosition() : 0,
        entity.getWatchedSeconds() != null ? entity.getWatchedSeconds() : 0,
        Boolean.TRUE.equals(entity.getIsCompleted())
    );
  }
}
