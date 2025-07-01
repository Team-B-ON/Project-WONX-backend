package io.github.bon.wonx.domain.profile.controller;

import io.github.bon.wonx.domain.follow.service.FollowService;
import io.github.bon.wonx.domain.profile.dto.ProfileUpdateRequest;
import io.github.bon.wonx.domain.profile.dto.PublicProfileDto;
import io.github.bon.wonx.domain.profile.service.ProfilePageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final ProfilePageService pageService;
    private final FollowService followService;

    private UUID currentUser(HttpServletRequest req) {
        UUID id = (UUID) req.getAttribute("userId");
        if (id == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return id;
    }

    @GetMapping
    public PublicProfileDto getMyProfile(HttpServletRequest req) {
        UUID me = (UUID) req.getAttribute("userId");
        return pageService.getProfileDetail(me, me);
    }

    @PatchMapping
    public ResponseEntity<PublicProfileDto> updateMyProfile(
            HttpServletRequest req,
            @RequestBody ProfileUpdateRequest dto
    ) {
        UUID userId = currentUser(req);
        PublicProfileDto updated = pageService.updateProfile(userId, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/followings/{followeeId}")
    public void follow(@PathVariable UUID followeeId, HttpServletRequest req) {
        followService.follow(currentUser(req), followeeId);
    }

    @DeleteMapping("/followings/{followeeId}")
    public void unfollow(@PathVariable UUID followeeId, HttpServletRequest req) {
        followService.unfollow(currentUser(req), followeeId);
    }

    // 내가 팔로잉하는 사람들
    @GetMapping("/followings")
    public ResponseEntity<List<PublicProfileDto>> getMyFollowings(HttpServletRequest req) {
        UUID me = currentUser(req);
        // FollowService에서 팔로잉 ID 리스트 조회
        List<UUID> followeeIds = followService.getFollowings(me);
        // 각 ID를 PublicProfileDto로 변환
        List<PublicProfileDto> dtos = followeeIds.stream()
                .map(id -> pageService.getProfileDetail(me, id))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<PublicProfileDto>> getMyFollowers(HttpServletRequest req) {
        UUID me = currentUser(req);
        List<UUID> followerIds = followService.getFollowers(me);
        List<PublicProfileDto> dtos = followerIds.stream()
                .map(id -> pageService.getProfileDetail(me, id))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}