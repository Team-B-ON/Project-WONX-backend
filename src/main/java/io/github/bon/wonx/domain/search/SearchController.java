package io.github.bon.wonx.domain.search;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final UserRepository userRepository;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> searchMovies(
        @RequestParam String query,
        @RequestParam(defaultValue = "relevance") String sort,
        HttpServletRequest request
    ) {
        UUID userId = extractUserId(request);
        return ResponseEntity.ok(searchService.searchMoviesByTitle(query, sort, userId));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<MovieDto>> searchByGenre(
        @RequestParam String query,
        @RequestParam(defaultValue = "relevance") String sort,
        HttpServletRequest request
    ) {
        UUID userId = extractUserId(request);
        return ResponseEntity.ok(searchService.searchMoviesByGenre(query, sort, userId));
    }

    @GetMapping("/people")
    public ResponseEntity<List<MovieDto>> searchByPerson(
        @RequestParam String query,
        @RequestParam(defaultValue = "relevance") String sort,
        HttpServletRequest request
    ) {
        UUID userId = extractUserId(request);
        return ResponseEntity.ok(searchService.searchMoviesByPerson(query, sort, userId));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> searchReviews(
        @RequestParam String query,
        HttpServletRequest request
    ) {
        UUID userId = extractUserId(request);
        return ResponseEntity.ok(searchService.searchReviews(query, userId));
    }

    @GetMapping("/autocomplete/movies")
    public ResponseEntity<List<String>> autocompleteMovies(@RequestParam String query) {
        return ResponseEntity.ok(searchService.autocompleteMovies(query));
    }

    @GetMapping("/autocomplete/people")
    public ResponseEntity<List<String>> autocompletePeople(@RequestParam String query) {
        return ResponseEntity.ok(searchService.autocompletePeople(query));
    }

    @GetMapping("/autocomplete/genres")
    public ResponseEntity<List<String>> autocompleteGenres(@RequestParam String query) {
        return ResponseEntity.ok(searchService.autocompleteGenres(query));
    }

    @GetMapping("/autocomplete/reviews")
    public ResponseEntity<List<String>> autocompleteReviews(@RequestParam String query) {
        return ResponseEntity.ok(searchService.autocompleteReviews(query));
    }

    // 🔒 공통 유저 ID 추출 메서드
    private UUID extractUserId(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }
}
