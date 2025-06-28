package io.github.bon.wonx.domain.follow.service;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.entity.FollowId;
import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepo;

    @Transactional
    public void follow(UUID followerId, UUID followeeId) {
        if (followerId.equals(followeeId)) return;
        if (followRepo.existsByIdFollowerIdAndIdFolloweeId(followerId, followeeId)) return;
        followRepo.save(new Follow(new FollowId(followerId, followeeId)));
    }

    @Transactional
    public void unfollow(UUID followerId, UUID followeeId) {
        followRepo.deleteByIdFollowerIdAndIdFolloweeId(followerId, followeeId);
    }
}
