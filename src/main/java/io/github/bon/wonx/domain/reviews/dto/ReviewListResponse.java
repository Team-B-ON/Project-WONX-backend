package io.github.bon.wonx.domain.reviews.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResponse {
    private ReviewStats stats;
    private int offset;
    private int limit;
    private String sort;
    private List<ReviewDto> results;
}
