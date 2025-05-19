package io.github.bon.wonx.domain.reviews;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private User user;
    private Movie movie;
    private Integer rating;
    private String content;
    private Boolean isAnonymous;
    private Boolean isDeleted = false;
    private LocalDateTime createdAt;

    public static ReviewDto from(Review review) {
        return new ReviewDto(
            review.getId(),
            review.getUser(),
            review.getMovie(),
            review.getRating(),
            review.getContent(),
            review.getIsAnonymous(),
            review.getIsDeleted(),
            review.getCreatedAt()
        );
    } 
}
