package io.github.bon.wonx.domain.movies.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.bon.wonx.domain.genres.dto.GenreDto;
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
    private Integer durationMinutes;

    @JsonProperty("genres")
    private List<GenreDto> genres;

    public static MovieSummaryDto from(Movie movie, boolean isBookmarked, boolean isLiked) {
        return new MovieSummaryDto(
            movie.getId(),
            movie.getTitle(),
            movie.getPosterUrl(),
            isBookmarked,
            isLiked,
            movie.getAgeRating(),
            movie.getDurationMinutes(),
            movie.getGenres().stream()
                    .map(GenreDto::from)
                    .toList()
        );
    }

    public static MovieSummaryDto from(MoviePerson rel, boolean isBookmarked, boolean isLiked) {
        return from(rel.getMovie(), isBookmarked, isLiked);
    }
}
