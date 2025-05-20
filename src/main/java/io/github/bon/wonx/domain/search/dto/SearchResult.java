package io.github.bon.wonx.domain.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchResult {

  private List<MovieSearchDto> movies;
  private List<UserSearchDto> users;
  // private List<ReviewSearchDto> reviews;
}
