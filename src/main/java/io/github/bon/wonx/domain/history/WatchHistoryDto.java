package io.github.bon.wonx.domain.history;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchHistoryDto {

  private MovieDto movie;     // ✅ MovieDto 전체 포함
  private int lastPosition;
  private int watchedSeconds;
  private boolean isCompleted;

  public static WatchHistoryDto from(WatchHistory entity) {
    return new WatchHistoryDto(
        MovieDto.from(entity.getMovie()),   // Movie → MovieDto로 변환
        entity.getLastPosition() != null ? entity.getLastPosition() : 0,
        entity.getWatchedSeconds() != null ? entity.getWatchedSeconds() : 0,
        Boolean.TRUE.equals(entity.getIsCompleted())
    );
  }
}
