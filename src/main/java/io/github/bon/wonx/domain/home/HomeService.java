package io.github.bon.wonx.domain.home;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.history.WatchHistory;
import io.github.bon.wonx.domain.history.WatchHistoryDto;
import io.github.bon.wonx.domain.history.WatchHistoryRepository;
import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.reviews.Review;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserPreferences;
import io.github.bon.wonx.domain.user.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final MovieRepository movieRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final ReviewRepository reviewRepository;

    public List<WatchHistoryDto> getRecentWatchHistory(User user) {
        List<WatchHistory> histories = watchHistoryRepository.findRecentHistoriesByUser(user.getId());
        return histories.stream()
                .map(WatchHistoryDto::from)
                .collect(Collectors.toList());
    }

    public MovieSummaryDto getBannerMovie(User user) {
        List<UUID> inProgressIds = watchHistoryRepository.findInProgressVideoIdsByUser(user.getId());

        if (!inProgressIds.isEmpty()) {
            UUID videoId = inProgressIds.get(0);
            return movieRepository.findById(videoId)
                    .map(m -> MovieSummaryDto.from(m, false, false))
                    .orElseThrow(() -> new IllegalStateException("이어보기 영화가 존재하지 않습니다."));
        }

        return movieRepository.findRandomMovie()
                .map(m -> MovieSummaryDto.from(m, false, false))
                .orElseThrow(() -> new IllegalStateException("랜덤 영화 조회 실패"));
    }

    public List<MovieSummaryDto> getHotMovies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByViewCountDesc();
        return movies.stream()
                .map(m -> MovieSummaryDto.from(m, false, false))
                .collect(Collectors.toList());
    }

    public List<MovieSummaryDto> getRecommendations(User user) {
        Optional<UserPreferences> preferencesOpt = userPreferencesRepository.findByUserId(user.getId());

        List<Movie> movies;
        if (preferencesOpt.isPresent()) {
            UserPreferences preferences = preferencesOpt.get();
            List<String> genres = extractGenresFromJson(preferences.getTopGenresJson());
            movies = movieRepository.findTop10ByGenres_NameInOrderByViewCountDesc(genres);
        } else {
            movies = movieRepository.findTop10ByOrderByViewCountDesc();
        }

        return movies.stream()
                .map(m -> MovieSummaryDto.from(m, false, false))
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getPopularReviews() {
        List<UUID> videoIds = reviewRepository.findPopularVideoIdsInLast24Hours(3);
        if (videoIds.isEmpty()) {
            videoIds = reviewRepository.findMostReviewedVideoIds(3);
        }

        List<Review> reviews = reviewRepository.findByMovieIds(videoIds);

        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .collect(Collectors.toMap(
                        r -> r.getMovie().getId(),
                        ReviewDto::from,
                        (r1, r2) -> r1
                ))
                .values()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public Long getTotalReviewCount() {
        return reviewRepository.count();
    }

    private List<String> extractGenresFromJson(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();

        return Arrays.stream(json.replaceAll("[\\[\\]\"]", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
