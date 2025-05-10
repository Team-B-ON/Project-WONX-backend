package io.github.bon.wonx.domain.video.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TmdbTrendingResponse {

  @JsonProperty
  private List<TrendingItem> results;

  @Getter
  public static class TrendingItem {
    private String title;

    @JsonProperty("name")
    private String name;
    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("first_air_date")
    private String firstAirDate;

    @JsonProperty("media_type")
    private String mediaType; // movie, tv, person
  }
}
