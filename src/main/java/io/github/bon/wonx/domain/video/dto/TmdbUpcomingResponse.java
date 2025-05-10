package io.github.bon.wonx.domain.video.dto;

import java.util.List;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class TmdbUpcomingResponse {

  // tmdb에서 받아오는 json의 결과 배열을 담기 위한 필드
  @JsonProperty("results")
  private List<TmdbMovie> results;

  // 응답의 각 영화 항목을 나타내는 내부 클래스
  @Getter
  public static class TmdbMovie {

    private String title;
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private String releaseDate;
  }
}
