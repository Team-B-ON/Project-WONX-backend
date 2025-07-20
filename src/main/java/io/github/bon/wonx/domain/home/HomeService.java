package io.github.bon.wonx.domain.home;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
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
        List<UUID> movieIds = histories.stream()
                .map(h -> h.getMovie().getId())
                .toList();

        List<UUID> bookmarkedIds = bookmarkRepository.findBookmarkedMovieIdsByUserAndMovieIds(user.getId(), movieIds);
        List<UUID> likedIds = likeRepository.findLikedMovieIdsByUserAndMovieIds(user.getId(), movieIds);

        return histories.stream()
                .map(h -> WatchHistoryDto.from(
                    h,
                    bookmarkedIds.contains(h.getMovie().getId()),
                    likedIds.contains(h.getMovie().getId())
                ))
                .toList();
    }

    public MovieSummaryDto getBannerMovie(User user) {
        List<UUID> inProgressIds = watchHistoryRepository.findInProgressVideoIdsByUser(user.getId());

        if (!inProgressIds.isEmpty()) {
            UUID videoId = inProgressIds.get(0);
            return homeRepository.findById(videoId)
                    .map(m -> MovieSummaryDto.from(
                        m,
                        bookmarkRepository.findByUserIdAndMovieId(user.getId(), m.getId()).isPresent(),
                        likeRepository.findByUserIdAndMovieId(user.getId(), m.getId()).isPresent()
                    ))
                    .orElseThrow(() -> new IllegalStateException("이어보기 영화가 존재하지 않습니다."));
        }

        return homeRepository.findRandomMovie()
                .map(m -> MovieSummaryDto.from(
                    m,
                    bookmarkRepository.findByUserIdAndMovieId(user.getId(), m.getId()).isPresent(),
                    likeRepository.findByUserIdAndMovieId(user.getId(), m.getId()).isPresent()
                ))
                .orElseThrow(() -> new IllegalStateException("랜덤 영화 조회 실패"));
    }

    public List<MovieSummaryDto> getHotMovies(UUID userId) {
        List<Movie> movies = homeRepository.findTop10ByOrderByViewCountDesc();
        return mapWithUserLikesAndBookmarks(movies, userId);
    }

    public List<ReviewDto> getPopularReviews() {
        List<UUID> videoIds = reviewRepository.findPopularVideoIdsInLast24Hours(PageRequest.of(0, 8));

        if (videoIds.isEmpty()) {
            videoIds = reviewRepository.findMostReviewedVideoIds(PageRequest.of(0, 8));
        }

        List<Review> reviews = reviewRepository.findByMovieIds(videoIds);

        return reviews.stream()
            .filter(r -> r.getMovie() != null && r.getUser() != null)
            .collect(Collectors.groupingBy(r -> r.getMovie().getId()))
            .values().stream()
            .map(rs -> rs.stream()
                        .max(Comparator.comparing(Review::getCreatedAt))
                        .orElse(null))
            .filter(r -> r != null)
            .map(ReviewDto::from)
            .limit(4)
            .toList();
    }

    public List<MovieSummaryDto> getRecommendations(User user) {
        List<Object[]> topGenresRaw = watchHistoryRepository.findTopGenresByUserId(user.getId());

        if (topGenresRaw.isEmpty()) {
            return getHotMovies(user.getId());
        }

        List<String> topGenres = topGenresRaw.stream()
            .map(row -> (String) row[0])
            .toList();

        List<Movie> movies = homeRepository.findTop10ByGenres_NameInOrderByViewCountDesc(topGenres);
        return mapWithUserLikesAndBookmarks(movies, user.getId());
    }

    public Long getTotalReviewCount() {
        return reviewRepository.count();
    }

    // ✅ 공통 처리 메서드: 북마크/좋아요 상태 반영
    private List<MovieSummaryDto> mapWithUserLikesAndBookmarks(List<Movie> movies, UUID userId) {
        List<UUID> ids = movies.stream().map(Movie::getId).toList();
        List<UUID> bookmarkedIds = bookmarkRepository.findBookmarkedMovieIdsByUserAndMovieIds(userId, ids);
        List<UUID> likedIds = likeRepository.findLikedMovieIdsByUserAndMovieIds(userId, ids);

        return movies.stream()
            .map(m -> MovieSummaryDto.from(
                m,
                bookmarkedIds.contains(m.getId()),
                likedIds.contains(m.getId())
            ))
            .toList();
    }
}
