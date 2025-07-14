package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.reviews.dto.ReviewCreateDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.reviews.dto.ReviewListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewsService;

    // 리뷰 조회
    @GetMapping("/api/movies/{id}/reviews")
    public ResponseEntity<ReviewListResponse> reviews(
            @PathVariable UUID id, 
            @RequestAttribute("userId") UUID userId,
            @RequestParam(defaultValue = "ratingDesc") String sort,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        ReviewListResponse response = reviewsService.reviews(id, userId, sort, offset, limit);
        return ResponseEntity.ok(response);
    }

    // 리뷰 작성
    @PostMapping("/api/movies/{id}/reviews")
    public ResponseEntity<ReviewDto> create(
            @PathVariable("id") UUID movieId, 
            @RequestAttribute("userId") UUID userId,
            @RequestBody @Valid ReviewCreateDto req) {
        ReviewDto createdDto = reviewsService.create(movieId, userId, req);
        return ResponseEntity.ok(createdDto);
    }

    // 리뷰 수정
    @PatchMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> update(@PathVariable("reviewId") UUID reviewId, @RequestBody ReviewDto dto) {
        ReviewDto updatedDto = reviewsService.update(reviewId, dto);
        return ResponseEntity.ok(updatedDto);
    }

    // 리뷰 삭제
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> delete(@PathVariable("reviewId") UUID reviewId) {
        ReviewDto deletedDto = reviewsService.delete(reviewId);
        return ResponseEntity.ok(deletedDto);
    }
}
