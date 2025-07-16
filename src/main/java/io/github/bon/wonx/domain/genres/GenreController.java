package io.github.bon.wonx.domain.genres;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}/movies")
    public ResponseEntity<?> getMoviesByGenre(
        @PathVariable UUID id,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) String groupBy,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "0") int limit,
        @RequestAttribute("userId") UUID userId
    ) {
        if ("subgenre".equals(groupBy)) {
            var res = genreService.getGroupedBySubgenre(id, userId, offset, limit);
            return ResponseEntity.ok(res);
        } else {
            var res = genreService.getSortedMovies(id, sort, userId, offset, limit);
            return ResponseEntity.ok(res);
        }
    }
    
}
