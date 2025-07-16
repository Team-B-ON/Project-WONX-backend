package io.github.bon.wonx.domain.genres.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SortedGenreMoviesResponse {
    private UUID genreId;
    private String genreName;
    private int offset;
    private int limit;
    private boolean hasNext;
    private String sort;
    private List<MovieSummaryDto> results;
}
