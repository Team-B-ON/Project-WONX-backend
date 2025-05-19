package io.github.bon.wonx.domain.genres;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.genres.dto.GenreMovieResponse;
import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    // 특정 장르의 영화 목록 조회
    public GenreMovieResponse moviesOf(UUID genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "영화 조회 실패: " + "대상 장르가 없습니다."));
        
        List<MovieSummaryDto> movies = genre.getMovies().stream()
                    .map(MovieSummaryDto::from)
                    .toList();

        return new GenreMovieResponse(
            genre.getId(),
            genre.getName(),
            0,
            movies.size(),
            false,
            "releaseDateDesc",
            movies
        );
    }
}
