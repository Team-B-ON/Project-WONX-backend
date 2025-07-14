package io.github.bon.wonx.domain.follow.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.entity.FollowId;

public record AdminFollowDto(
    UUID followerId,
    UUID followeeId,
    LocalDateTime createdAt
) {
    public static AdminFollowDto fromEntity(Follow follow) {
        FollowId id = follow.getId();
        return new AdminFollowDto(
            id.getFollowerId(),
            id.getFolloweeId(),
            follow.getCreatedAt()
        );
    }
}
