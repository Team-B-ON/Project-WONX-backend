package io.github.bon.wonx.domain.home;

import java.util.Comparator;
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
    // 1. 최근 24시간 내에 리뷰가 달린 영화 ID 최대 8개 조회
    List<UUID> videoIds = reviewRepository.findPopularVideoIdsInLast24Hours(8);

    // 2. 없으면 → 리뷰 많은 영화 기준으로 대체
    if (videoIds.isEmpty()) {
        videoIds = reviewRepository.findMostReviewedVideoIds(8);
    }

    // 3. 해당 영화들의 리뷰 가져오기
    List<Review> reviews = reviewRepository.findByMovieIds(videoIds);

    // 4. 영화별로 리뷰 그룹핑 후 최신 리뷰 하나씩만 추출
    return reviews.stream()
        .filter(r -> r.getMovie() != null && r.getUser() != null) // null 방어
        .collect(Collectors.groupingBy(r -> r.getMovie().getId()))
        .values().stream()
        .map(rs -> rs.stream()
                     .max(Comparator.comparing(Review::getCreatedAt))
                     .orElseThrow()) // 최신 리뷰 선택
        .map(ReviewDto::from)
        .limit(4) // 프론트 기준 슬라이더 4개
        .toList();
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
