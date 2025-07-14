package io.github.bon.wonx.domain.movies.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/movies")
public class AdminMovieController {

    private final MovieRepository movieRepository;

    @GetMapping
    public ResponseEntity<List<AdminMovieDto>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return ResponseEntity.ok(movies.stream().map(AdminMovieDto::fromEntity).toList());
    }
}
