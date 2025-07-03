package io.github.bon.wonx.domain.follow.service;

import java.util.List;
import java.util.UUID;

public interface FollowService {
    List<UUID> getFollowings(UUID userId);
    List<UUID> getFollowers(UUID userId);
    void follow(UUID followerId, UUID followeeId);
    void unfollow(UUID followerId, UUID followeeId);
}
