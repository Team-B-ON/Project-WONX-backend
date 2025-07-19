package io.github.bon.wonx.domain.home;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.history.WatchHistoryDto;
import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;
    private final UserRepository userRepository;

    @GetMapping("/banner")
    public ResponseEntity<MovieSummaryDto> getMainBanner(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getBannerMovie(user));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<WatchHistoryDto>> getRecentWatchHistory(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getRecentWatchHistory(user));
    }

    @GetMapping("/hot-movies")
    public ResponseEntity<List<MovieSummaryDto>> getHotMovies(
        @RequestAttribute("userId") UUID userId
    ) {
        return ResponseEntity.ok(homeService.getHotMovies(userId));
    }

    @GetMapping("/popular-reviews")
    public ResponseEntity<List<ReviewDto>> getPopularReviews() {
        return ResponseEntity.ok(homeService.getPopularReviews());
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<MovieSummaryDto>> getRecommendations(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getRecommendations(user));
    }

    @GetMapping("/review-count")
    public ResponseEntity<Long> getTotalReviewCount() {
        return ResponseEntity.ok(homeService.getTotalReviewCount());
    }
}
