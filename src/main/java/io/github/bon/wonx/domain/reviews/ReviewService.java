package io.github.bon.wonx.domain.reviews;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.reviews.dto.ReviewCreateDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewListResponse;
import io.github.bon.wonx.domain.reviews.dto.ReviewStats;
import io.github.bon.wonx.domain.reviews.dto.ReviewUpdateDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    // 정렬 유틸 함수
    private List<Review> sortReviews(List<Review> list, String sort) {
        return switch (sort) {
            case "latest" -> list.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .toList();
            case "ratingAsc" -> list.stream()
                .sorted(Comparator.comparing(Review::getRating))
                .toList();
            case "ratingDesc" -> list.stream()
                .sorted(Comparator.comparing(Review::getRating).reversed())
                .toList();
            default -> list;
        };
    }

    // 통계 유틸 함수
    private Map<String, Integer> calculateDistribution(List<Review> list) {
        Map<String, Integer> dist = new LinkedHashMap<>();
        dist.put("9-10", 0);
        dist.put("7-8", 0);
        dist.put("5-6", 0);
        dist.put("3-4", 0);
        dist.put("1-2", 0);

        for (Review r : list) {
            int rating = r.getRating();
            if (rating >= 9) dist.computeIfPresent("9-10", (k, v) -> v + 1);
            else if (rating >= 7) dist.computeIfPresent("7-8", (k, v) -> v + 1);
            else if (rating >= 5) dist.computeIfPresent("5-6", (k, v) -> v + 1);
            else if (rating >= 3) dist.computeIfPresent("3-4", (k, v) -> v + 1);
            else dist.computeIfPresent("1-2", (k, v) -> v + 1);
        }
        return dist;
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public ReviewListResponse reviews(UUID movieId, UUID currentUserId, String sort, int offset, int limit) {
        List<Review> all = reviewRepository.findByMovieId(movieId);
        
        // 정렬
        List<Review> sorted = sortReviews(all, sort);
        // 페이징
        List<Review> paged = sorted.stream()
            .skip(offset)
            .limit(limit)
            .toList();
        // DTO 변환
        List<ReviewDto> results = paged.stream()
            .map(r -> ReviewDto.from(r, currentUserId))
            .toList();
        // 통계
        double avg = all.stream().mapToInt(Review::getRating).average().orElse(0);
        int count = all.size();
        Map<String, Integer> dist = calculateDistribution(all);

        return new ReviewListResponse(
            new ReviewStats(avg, count, dist),
            offset,
            limit,
            sort,
            results
        );
    }

    // 댓글 생성
    @Transactional
    public ReviewDto create(UUID movieId, UUID userId, ReviewCreateDto req) {
        Movie movie = movieRepository.findById(movieId) 
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 생성 실패: " + "대상 영화가 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 생성 실패: " + "대상 유저가 없습니다."));
        Review review = new Review(
            user, 
            movie, 
            req.getRating(),
            req.getContent(),
            Boolean.TRUE.equals(req.getIsAnonymous())
        );
        Review created = reviewRepository.saveAndFlush(review);
        return ReviewDto.from(created, userId);
    }

    // 댓글 수정
    @Transactional
    public ReviewDto update(UUID reviewId, UUID currentUserId, ReviewUpdateDto req) {
        Review target = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 수정 실패: " + "대상 댓글이 없습니다."));
        target.patch(req.getRating(), req.getContent());
        Review updated = reviewRepository.save(target);
        return ReviewDto.from(updated, currentUserId);
    }

    // 댓글 삭제
    @Transactional
    public void delete(UUID id) {
        Review target = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 삭제 실패: " + "대상 댓글이 없습니다."));
        reviewRepository.delete(target);
    }
}
