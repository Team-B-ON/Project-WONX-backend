package io.github.bon.wonx.domain.reviews;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private UUID userId;
    private String userNickname;
    private UUID movieId;
    private Integer rating;
    private String content;
    private Boolean isAnonymous;
    private Boolean isDeleted = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static ReviewDto from(Review review) {
        String userNickname = review.getIsAnonymous() 
            ? "익명" 
            : review.getUser().getNickname();

        return new ReviewDto(
            review.getId(),
            review.getUser().getId(),
            userNickname,
            review.getMovie().getId(),
            review.getRating(),
            review.getContent(),
            review.getIsAnonymous(),
            review.getIsDeleted(),
            review.getCreatedAt()
        );
    } 
}
