package io.github.bon.wonx.domain.home;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.history.WatchHistoryDto;
import io.github.bon.wonx.domain.home.dto.BoxOfficeDto;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.home.dto.RecommendDto;
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
    public ResponseEntity<?> getMainBanner() {
        return ResponseEntity.ok(homeService.getBannerMovie());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<?>> getUpcomingMovies() {
        return ResponseEntity.ok(homeService.getUpcomingMovies());
    }

    @GetMapping("/hot-movies")
    public ResponseEntity<List<HotMovieDto>> getHotMovies() {
        return ResponseEntity.ok(homeService.getHotMovies());
    }

    @GetMapping("/popular-reviews") // 기존 hot-talks에서 변경
    public ResponseEntity<List<ReviewDto>> getPopularReviews() {
        return ResponseEntity.ok(homeService.getPopularReviews());
    }

    @GetMapping("/box-office")
    public ResponseEntity<List<BoxOfficeDto>> getBoxOfficeMovies() {
        return ResponseEntity.ok(homeService.getBoxOfficeMovies());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<WatchHistoryDto>> getRecentWatchHistory(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getRecentWatchHistory(user));
    }

    @GetMapping("/continue")
    public ResponseEntity<List<WatchHistoryDto>> getContinueWatching(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getContinueWatching(user));
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<RecommendDto>> getRecommendations(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(homeService.getRecommendations(user));
    }

    @GetMapping("/review-count")
    public ResponseEntity<Long> getTotalReviewCount() {
        return ResponseEntity.ok(homeService.getTotalReviewCount());
    }
}
