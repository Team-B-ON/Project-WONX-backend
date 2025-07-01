package io.github.bon.wonx.domain.follow.service;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.entity.FollowId;
import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepo;

    @Override
    public List<UUID> getFollowings(UUID userId) {
        return followRepo.findAllById_FollowerId(userId)
                .stream()
                .map(f -> f.getId().getFolloweeId())
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> getFollowers(UUID userId) {
        return followRepo.findAllById_FolloweeId(userId)
                .stream()
                .map(f -> f.getId().getFollowerId())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void follow(UUID followerId, UUID followeeId) {
        log.debug("▶ follow() 진입: {} → {}", followerId, followeeId);
        if (followerId.equals(followeeId)) return;
        if (followRepo.existsById_FollowerIdAndId_FolloweeId(followerId, followeeId)) return;
        followRepo.save(new Follow(new FollowId(followerId, followeeId)));
        log.debug("▶ follow() 저장 완료");
    }

    @Override
    @Transactional
    public void unfollow(UUID followerId, UUID followeeId) {
        followRepo.deleteById_FollowerIdAndId_FolloweeId(followerId, followeeId);
    }
}
