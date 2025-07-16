package io.github.bon.wonx.domain.genres.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.genres.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private UUID id;
    private String name;

    public static GenreDto from(Genre genre) {
        return new GenreDto(
            genre.getId(),
            genre.getName()
        );
    }
}
