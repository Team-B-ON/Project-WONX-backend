package io.github.bon.wonx.domain.movies.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.genres.dto.GenreDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.people.Person;
import io.github.bon.wonx.domain.people.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDetailDto {
    private UUID movieId;
    private String title;
    private String posterUrl;
    private String mainImg;
    private String releaseDate;
    private Integer durationMinutes;
    private String ageRating;
    private String description;

    private List<PersonDto> actors;
    private List<GenreDto> genres;
    private List<PersonDto> directors;
    private List<PersonDto> screenwriters;

    private boolean isBookmarked;
    private boolean isLiked;

    public static MovieDetailDto from(
        Movie movie,
        List<Person> actors,
        List<Person> directors,
        List<Person> screenwriters,
        boolean isBookmarked,
        boolean isLiked
    ) {
        return MovieDetailDto.builder()
            .movieId(movie.getId())
            .title(movie.getTitle())
            .posterUrl(movie.getPosterUrl())
            .mainImg(movie.getMainImg())
            .releaseDate(movie.getReleaseDate().toString())
            .durationMinutes(movie.getDurationMinutes())
            .ageRating(movie.getAgeRating())
            .description(movie.getDescription())
            .actors(actors.stream().map(PersonDto::from).toList())
            .directors(directors.stream().map(PersonDto::from).toList())
            .screenwriters(screenwriters.stream().map(PersonDto::from).toList())
            .genres(movie.getGenres().stream().map(GenreDto::from).toList())
            .isBookmarked(isBookmarked)
            .isLiked(isLiked)
            .build();
    }
}
