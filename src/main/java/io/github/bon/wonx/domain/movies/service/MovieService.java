package io.github.bon.wonx.domain.movies.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    // 영화 상세 정보 조회
    public MovieDto details(UUID id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        return MovieDto.from(movie);
    }

    // 함께 시청된 콘텐츠 조회
    public List<MovieDto> relatedContents(UUID id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null || movie.getGenres().isEmpty()) return List.of();

        List<Genre> genres = movie.getGenres();
        List<Movie> related = movieRepository.findByGenresIn(genres);

        return related.stream()
            .filter(m -> !m.getId().equals(id))
            .map(MovieDto::from)
            .toList();
    }    
}
