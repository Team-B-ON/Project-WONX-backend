package io.github.bon.wonx.domain.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewUpdateResponse {
    private ReviewDto review;
    private String message;

    public static ReviewUpdateResponse from(ReviewDto dto) {
        return new ReviewUpdateResponse(dto, "리뷰 수정 완료");
    }
    
}
