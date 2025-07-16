package io.github.bon.wonx.domain.search.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.reviews.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewSearchDto {

    private UUID id;
    private String content;

    private String movieTitle;
    private String posterUrl;

    private String author;
    private Integer rating;

    public static ReviewSearchDto from(Review review) {
        return ReviewSearchDto.builder()
            .id(review.getId())
            .content(review.getContent() != null ? review.getContent() : "")
            .movieTitle(review.getMovie().getTitle())
            .posterUrl(review.getMovie().getPosterUrl())
            .author(review.getIsAnonymous() ? "익명" : review.getUser().getNickname())
            .rating(review.getRating())
            .build();
    }
}
