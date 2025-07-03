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

@Service
@RequiredArgsConstructor
public class ProfilePageService {

    private final UserRepository    userRepo;
    private final FollowRepository  followRepo;

    @Transactional
    public PublicProfileDto getProfileDetail(UUID requesterId, UUID targetId) {
        User user = userRepo.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return buildDto(user, requesterId);
    }

    private PublicProfileDto buildDto(User user, UUID requesterId) {
        UUID profileId = user.getId();
        boolean isMe = profileId.equals(requesterId);
        boolean isFollowing = !isMe && followRepo.existsById_FollowerIdAndId_FolloweeId(requesterId, profileId);
        long followerCount = followRepo.countById_FolloweeId(profileId);
        long followingCount = followRepo.countById_FollowerId(profileId);

        return PublicProfileDto.builder()
                .userId(profileId)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .joinedAt(user.getCreatedAt())
                .isMe(isMe)
                .isFollowing(isFollowing)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    @Transactional
    public PublicProfileDto updateProfile(UUID userId, ProfileUpdateRequest dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.getNickname() != null) {
            user.updateNickname(dto.getNickname());
        }
        if (dto.getBio() != null) {
            user.updateBio(dto.getBio());
        }
        if (dto.getProfileImageUrl() != null) {
            user.updateProfileImageUrl(dto.getProfileImageUrl());
        }

        // 변경된 엔티티를 DTO로 변환하여 반환
        return buildDto(user, userId);
    }
}
