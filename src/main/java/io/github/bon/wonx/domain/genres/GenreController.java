package io.github.bon.wonx.domain.genres;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.genres.dto.GenreMovieResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    // 특정 장르의 영화 목록 조회
    @GetMapping("/api/genres/{genreId}/movies")
    public ResponseEntity<GenreMovieResponse> getMoviesByGenre(
        @PathVariable UUID genreId,
        @RequestParam(defaultValue = "releaseDateDesc") String sort,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "100") int limit        
    ) {
        return ResponseEntity.ok(genreService.moviesOf(genreId));
    }
}
