package io.github.bon.wonx.domain.history;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchHistoryDto {

  private MovieSummaryDto movie;
  private int lastPosition;
  private int watchedSeconds;
  private boolean isCompleted;

  public static WatchHistoryDto from(WatchHistory entity) {
    return new WatchHistoryDto(
        MovieSummaryDto.from(entity.getMovie(), false, false),
        entity.getLastPosition() != null ? entity.getLastPosition() : 0,
        entity.getWatchedSeconds() != null ? entity.getWatchedSeconds() : 0,
        Boolean.TRUE.equals(entity.getIsCompleted())
    );
  }
}
