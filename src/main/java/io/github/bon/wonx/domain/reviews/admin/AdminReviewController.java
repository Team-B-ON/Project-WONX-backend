package io.github.bon.wonx.domain.reviews.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.reviews.Review;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<List<AdminReviewDto>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<AdminReviewDto> result = reviews.stream()
                .map(AdminReviewDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
