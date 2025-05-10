package io.github.bon.wonx.domain.review.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.bon.wonx.domain.review.dto.TrendingReviewResponse;
import io.github.bon.wonx.domain.review.service.ReviewService;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class ReviewController {

  private final ReviewService reviewService;

  // 트렌딩 리뷰 API (기본 10개)
  @GetMapping("/trending-reviews")
  public List<TrendingReviewResponse> getTrendingReviews(
      @RequestParam(defaultValue = "10") int count) {
    return reviewService.getTrendingReviews(count);
  }
}
