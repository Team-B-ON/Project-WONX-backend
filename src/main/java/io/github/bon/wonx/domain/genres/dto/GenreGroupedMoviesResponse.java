package io.github.bon.wonx.domain.genres.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenreGroupedMoviesResponse {
    private UUID genreId;
    private String genreName;
    private int offset;
    private int limit;
    private boolean hasNext;
    private String groupBy = "subgenre";
    private List<SubgenreGroup> groups;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubgenreGroup {
        private String subgenre;
        private UUID subgenreId;
        private List<MovieSummaryDto> movies;
    }
}
