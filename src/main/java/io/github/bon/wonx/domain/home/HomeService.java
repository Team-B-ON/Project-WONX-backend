package io.github.bon.wonx.domain.home;

import java.time.LocalDate;
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
import io.github.bon.wonx.domain.home.dto.BoxOfficeDto;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.home.dto.RecommendDto;
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

    public List<BoxOfficeDto> getBoxOfficeMovies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByBoxOfficeRankAsc();
        return movies.stream()
                .map(m -> new BoxOfficeDto(
                        m.getId(),
                        m.getTitle(),
                        m.getPosterUrl(),
                        m.getBoxOfficeRank() != null ? m.getBoxOfficeRank().longValue() : 0L
                ))
                .collect(Collectors.toList());
    }

    public List<HotMovieDto> getHotMovies() {
        List<Movie> movies = movieRepository.findTop10ByOrderByViewCountDesc();
        return movies.stream()
                .map(HotMovieDto::from)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getPopularReviews() {
        // 1. 최근 24시간 내 리뷰가 많이 달린 영화 ID 조회
        List<UUID> videoIds = reviewRepository.findPopularVideoIdsInLast24Hours(3);

        // 2. 없다면 전체 리뷰 수 기준 인기 영화로 대체
        if (videoIds.isEmpty()) {
            videoIds = reviewRepository.findMostReviewedVideoIds(3);
        }

        // 3. 영화 ID들에 해당하는 리뷰 중 최신 순 정렬 → 각 영화당 1개만 추출
        List<Review> reviews = reviewRepository.findByMovieIds(videoIds);

        // 최신 리뷰들 중에서 videoId 별로 대표 1개씩만 추출
        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt())) // 최신 순
                .collect(Collectors.toMap(
                        r -> r.getMovie().getId(), // key: movieId
                        ReviewDto::from,           // value
                        (r1, r2) -> r1             // 중복 시 첫 번째 값 유지
                ))
                .values()
                .stream()
                .limit(3) // 혹시 3개 넘는 경우
                .collect(Collectors.toList());
    }

    public List<WatchHistoryDto> getRecentWatchHistory(User user) {
        List<WatchHistory> histories = watchHistoryRepository.findRecentHistoriesByUser(user.getId());
        return histories.stream()
                .map(WatchHistoryDto::from)
                .collect(Collectors.toList());
    }

    public List<WatchHistoryDto> getContinueWatching(User user) {
        List<WatchHistory> histories = watchHistoryRepository.findInProgressHistoriesByUser(user.getId());
        return histories.stream()
                .map(WatchHistoryDto::from)
                .collect(Collectors.toList());
    }

    public List<RecommendDto> getRecommendations(User user) {
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
                .map(m -> new RecommendDto(
                        m.getId(),
                        m.getTitle(),
                        m.getPosterUrl(),
                        m.getBoxOfficeRank() != null ? m.getBoxOfficeRank() : 0
                ))
                .collect(Collectors.toList());
    }

    public HotMovieDto getBannerMovie() {
        return movieRepository.findTop1ByOrderByBoxOfficeRankAsc()
                .map(HotMovieDto::from)
                .orElseThrow(() -> new IllegalStateException("No banner movie found"));
    }

    public List<HotMovieDto> getUpcomingMovies() {
        LocalDate now = LocalDate.now();
        LocalDate threeWeeksLater = now.plusWeeks(3);
        return movieRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(now, threeWeeksLater)
                .stream()
                .map(HotMovieDto::from)
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
