package io.github.bon.wonx.domain.movies.admin;

import java.time.LocalDate;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;

public record AdminMovieDto(
    UUID id,
    String title,
    Float rating,
    Integer durationMinutes,
    LocalDate releaseDate,
    Integer viewCount
) {
    public static AdminMovieDto fromEntity(Movie movie) {
        return new AdminMovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getRating(),
            movie.getDurationMinutes(),
            movie.getReleaseDate(),
            movie.getViewCount()
        );
    }
}
