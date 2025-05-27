package io.github.bon.wonx.domain.profile.service;

import java.util.UUID;

import io.github.bon.wonx.domain.profile.DTO.PublicProfileDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfilePageService {
    private final UserRepository userRepo;

    public ProfilePageService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public PublicProfileDto getProfileDetail(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));
        // 2) 취향분석 요약
        // 3) 팔로우/팔로잉 카운트
        // 4) DTO 조립
        return PublicProfileDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .joinedAt(user.getCreatedAt())
                // followerCount()
                // followingCount()
                .build();
    }
}
