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
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
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
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;

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

    public List<MovieSummaryDto> getHotMovies(UUID userId) {
        List<Movie> movies = homeRepository.findTop10ByOrderByViewCountDesc();

        if (movies.isEmpty()) return List.of();

        List<UUID> movieIds = movies.stream().map(Movie::getId).toList();

        List<UUID> bookmarkedIds = bookmarkRepository.findBookmarkedMovieIdsByUserAndMovieIds(userId, movieIds);
        List<UUID> likedIds = likeRepository.findLikedMovieIdsByUserAndMovieIds(userId, movieIds);

        return movies.stream()
                .map(m -> MovieSummaryDto.from(
                    m, 
                    bookmarkedIds.contains(m.getId()), 
                    likedIds.contains(m.getId())
                ))
                .toList();
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
            return getHotMovies(user.getId());  // fallback
        }

        List<String> topGenres = topGenresRaw.stream()
            .map(row -> (String) row[0])
            .toList();

        List<Movie> movies = homeRepository.findTop10ByGenres_NameInOrderByViewCountDesc(topGenres);

        if (movies.isEmpty()) return List.of();

        List<UUID> movieIds = movies.stream().map(Movie::getId).toList();

        List<UUID> bookmarkedIds = bookmarkRepository.findBookmarkedMovieIdsByUserAndMovieIds(user.getId(), movieIds);
        List<UUID> likedIds = likeRepository.findLikedMovieIdsByUserAndMovieIds(user.getId(), movieIds);

        return movies.stream()
            .map(m -> MovieSummaryDto.from(
                m, 
                bookmarkedIds.contains(m.getId()), 
                likedIds.contains(m.getId())
            ))
            .toList();
    }

    public Long getTotalReviewCount() {
        return reviewRepository.count();
    }
}
