package io.github.bon.wonx.domain.movies.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MoviePerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSummaryDto {
    private UUID movieId;
    private String title;
    private String posterUrl;
    private boolean isBookmarked;
    private boolean isLiked;
    private String ageRating;
    private Integer duration;
    private List<String> genres;

    public static MovieSummaryDto from(MoviePerson rel) {
        Movie movie = rel.getMovie();
        
        return new MovieSummaryDto(
            movie.getId(),
            movie.getTitle(),
            movie.getPosterUrl(),
            false,
            false,
            movie.getAgeRating(),
            movie.getDurationMinutes(),
            movie.getGenres().stream()
                    .map(genre -> genre.getName())
                    .toList()
        );
    }
}
