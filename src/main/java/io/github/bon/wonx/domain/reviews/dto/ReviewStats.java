package io.github.bon.wonx.domain.reviews.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewStats {
    private double averageRating;
    private int totalCount;
    private Map<String, Integer> distribution;
}
