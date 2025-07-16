package io.github.bon.wonx.domain.movies.dto;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.genres.dto.GenreDto;
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
}
