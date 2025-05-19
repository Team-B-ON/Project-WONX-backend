package io.github.bon.wonx.domain.movies.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private UUID id;
    private UUID userId;
    private UUID movieId;
    private LocalDateTime createdAt;
    private boolean isLiked;
    
    public static LikeDto from(Like like) {
        return new LikeDto(
            like.getId(),
            like.getUser().getId(),
            like.getMovie().getId(),
            like.getCreatedAt(),
            true
        );
    }

    public static LikeDto notLiked(UUID userId, UUID movieId) {
        return new LikeDto(
            null,
            userId,
            movieId,
            null,
            false
        );
    }
}

