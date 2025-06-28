package io.github.bon.wonx.domain.profile.service;

import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import io.github.bon.wonx.domain.profile.dto.*;
import io.github.bon.wonx.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class ProfilePageService {

    private final UserRepository    userRepo;
    private final FollowRepository  followRepo;

    public PublicProfileDto getProfileDetail(UUID targetId, UUID viewerId) {

        User target = userRepo.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        boolean isMe        = viewerId != null && targetId.equals(viewerId);
        boolean isFollowing = !isMe && viewerId != null &&
                followRepo.existsByIdFollowerIdAndIdFolloweeId(viewerId, targetId);

        long followers  = followRepo.countByIdFolloweeId(targetId);
        long followings = followRepo.countByIdFollowerId(targetId);

        return PublicProfileDto.builder()
                .userId(target.getId())
                .email(target.getEmail())
                .nickname(target.getNickname())
                .profileImageUrl(target.getProfileImageUrl())
                .bio(target.getBio())
                .joinedAt(target.getCreatedAt())
                .isMe(isMe)
                .isFollowing(isFollowing)
                .followerCount(followers)
                .followingCount(followings)
                .build();
    }

    @Transactional
    public void updateProfile(UUID userId, ProfileUpdateRequest dto) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.getNickname()        != null) user.updateNickname(dto.getNickname());
        if (dto.getBio()             != null) user.updateBio(dto.getBio());
        if (dto.getProfileImageUrl() != null) user.updateProfileImageUrl(dto.getProfileImageUrl());
    }
}
