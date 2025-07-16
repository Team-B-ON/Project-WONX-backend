package io.github.bon.wonx.domain.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewDeleteResponse {
    private String message;

    public static ReviewDeleteResponse success() {
        return new ReviewDeleteResponse("리뷰 삭제 완료");
    }
}
