package io.github.bon.wonx.domain.people.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonMovieResponse {
    private UUID personId;
    private String name;
    private List<String> role;
    private List<MovieSummaryDto> movies;
}
