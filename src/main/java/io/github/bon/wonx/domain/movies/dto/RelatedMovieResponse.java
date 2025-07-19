package io.github.bon.wonx.domain.movies.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatedMovieResponse {
    private long total;
    private int offset;
    private int limit;
    private List<MovieDto> results;    
}
