package io.github.bon.wonx.domain.follow.repository;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    boolean existsByIdFollowerIdAndIdFolloweeId(UUID followerId, UUID followeeId);

    long countByIdFolloweeId(UUID followeeId);

    long countByIdFollowerId(UUID followerId);

    void deleteByIdFollowerIdAndIdFolloweeId(UUID followerId, UUID followeeId);
}
