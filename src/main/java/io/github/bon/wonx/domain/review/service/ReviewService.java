package io.github.bon.wonx.domain.review.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.review.dto.TrendingReviewResponse;
import io.github.bon.wonx.domain.review.entity.Review;
import io.github.bon.wonx.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  // 리뷰 하나하나를 TrendingReviewResponse로 변환
  public List<TrendingReviewResponse> getTrendingReviews(int count) {
    return reviewRepository.findAllByOrderByViewCountDescCreatedAtDesc(PageRequest.of(0, count))
        .stream()
        .map(review -> TrendingReviewResponse.builder()
            .reviewId(review.getId())
            .content(review.getContent())
            .viewCount(review.getViewCount())
            .createdAt(review.getCreatedAt().toString())
            .videoId(review.getVideoId())
            .userId(review.getUserId())
            .build())
        .toList();
  }

}
