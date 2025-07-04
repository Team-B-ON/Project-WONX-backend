package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewsService;

    // 리뷰 조회
    @GetMapping("/api/movies/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> reviews(@PathVariable UUID id) {
        List<ReviewDto> dtos = reviewsService.reviews(id);
        return ResponseEntity.ok(dtos);
    }

    // 리뷰 작성
    @PostMapping("/api/movies/{id}/reviews")
    public ResponseEntity<ReviewDto> create(@PathVariable UUID id, @RequestBody ReviewDto dto) {
        ReviewDto createdDto = reviewsService.create(id, dto);
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
