package io.github.bon.wonx.domain.home;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.history.WatchHistory;
import io.github.bon.wonx.domain.history.WatchHistoryDto;
import io.github.bon.wonx.domain.history.WatchHistoryRepository;
import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.reviews.Review;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final HomeRepository homeRepository;
    private final WatchHistoryRepository watchHistoryRepository;
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
            return homeRepository.findById(videoId)
                    .map(m -> MovieSummaryDto.from(m, false, false))
                    .orElseThrow(() -> new IllegalStateException("이어보기 영화가 존재하지 않습니다."));
        }

        return homeRepository.findRandomMovie()
                .map(m -> MovieSummaryDto.from(m, false, false))
                .orElseThrow(() -> new IllegalStateException("랜덤 영화 조회 실패"));
    }

    public List<MovieSummaryDto> getHotMovies() {
        List<Movie> movies = homeRepository.findTop10ByOrderByViewCountDesc();
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

    public List<MovieSummaryDto> getRecommendations(User user) {
        List<Object[]> topGenresRaw = watchHistoryRepository.findTopGenresByUserId(user.getId());

        if (topGenresRaw.isEmpty()) {
            return getHotMovies();  // fallback
        }

        List<String> topGenres = topGenresRaw.stream()
            .map(row -> (String) row[0])
            .collect(Collectors.toList());

        List<Movie> movies = homeRepository.findTop10ByGenres_NameInOrderByViewCountDesc(topGenres);

        return movies.stream()
            .map(m -> MovieSummaryDto.from(m, false, false))
            .collect(Collectors.toList());
    }

    public Long getTotalReviewCount() {
        return reviewRepository.count();
    }
}
