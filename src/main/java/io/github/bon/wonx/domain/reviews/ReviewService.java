package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    // 댓글 조회
    public List<ReviewDto> reviews(UUID id) {
        return reviewRepository.findByMovieId(id)
                .stream()
                .map(review -> ReviewDto.from(review))
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public ReviewDto create(UUID id, ReviewDto dto) {
        Movie movie = movieRepository.findById(id) 
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 생성 실패: " + "대상 영화가 없습니다."));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 생성 실패: " + "대상 유저가 없습니다."));
        Review review = Review.createReview(dto, movie, user);
        Review created = reviewRepository.save(review);
        return ReviewDto.from(created);
    }

    // 댓글 수정
    @Transactional
    public ReviewDto update(UUID id, ReviewDto dto) {
        Review target = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 수정 실패: " + "대상 댓글이 없습니다."));
        target.patch(dto);
        Review updated = reviewRepository.save(target);
        return ReviewDto.from(updated);
    }

    // 댓글 삭제
    @Transactional
    public ReviewDto delete(UUID id) {
        Review target = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "댓글 삭제 실패: " + "대상 댓글이 없습니다."));
        reviewRepository.delete(target);
        return ReviewDto.from(target);
    }
}
