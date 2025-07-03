package io.github.bon.wonx.domain.follow.repository;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    // 팔로잉 관계가 있는지 체크
    boolean existsById_FollowerIdAndId_FolloweeId(UUID followerId, UUID followeeId);

    // 언팔로우
    void deleteById_FollowerIdAndId_FolloweeId(UUID followerId, UUID followeeId);

    // 내가 팔로잉하는 사람들 조회
    List<Follow> findAllById_FollowerId(UUID followerId);

    // 나를 팔로우하는 사람들 조회
    List<Follow> findAllById_FolloweeId(UUID followeeId);

    // 팔로워/팔로잉 수 집계
    long countById_FolloweeId(UUID followeeId);
    long countById_FollowerId(UUID followerId);
}
