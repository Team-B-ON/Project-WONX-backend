package io.github.bon.wonx.domain.reviews.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.bon.wonx.domain.reviews.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    @JsonProperty("reviewId")
    private UUID id;

    private UUID userId;
    private String userNickname;
    private UUID movieId;
    private Integer rating;
    private String content;
    private Boolean isAnonymous;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private Boolean isMine;

    public static ReviewDto from(Review review) {
        return from(review, null);
    }

    public static ReviewDto from(Review review, UUID currentUserId) {
        String userNickname = review.getIsAnonymous() 
            ? "익명" 
            : review.getUser().getNickname();
        
        boolean isMine = currentUserId != null && review.getUser().getId().equals(currentUserId);

        return new ReviewDto(
            review.getId(),
            review.getUser().getId(),
            userNickname,
            review.getMovie().getId(),
            review.getRating(),
            review.getContent(),
            review.getIsAnonymous(),
            review.getCreatedAt(),
            isMine
        );
    } 
}
