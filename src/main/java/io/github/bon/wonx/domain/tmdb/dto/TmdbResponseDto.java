package io.github.bon.wonx.domain.tmdb.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TmdbResponseDto {

  private List<TmdbMovieDto> results;
}
