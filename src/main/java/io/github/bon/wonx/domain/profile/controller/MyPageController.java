package io.github.bon.wonx.domain.profile.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.bon.wonx.domain.follow.service.FollowService;
import io.github.bon.wonx.domain.profile.dto.ProfileUpdateRequest;
import io.github.bon.wonx.domain.profile.dto.PublicProfileDto;
import io.github.bon.wonx.domain.profile.service.ProfilePageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

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
    public void updateMyProfile(HttpServletRequest req,
                                @RequestBody ProfileUpdateRequest dto) {
        pageService.updateProfile(currentUser(req), dto);
    }

    @PostMapping("/followings/{followeeId}")
    public void follow(@PathVariable UUID followeeId, HttpServletRequest req) {
        followService.follow(currentUser(req), followeeId);
    }

    @DeleteMapping("/followings/{followeeId}")
    public void unfollow(@PathVariable UUID followeeId, HttpServletRequest req) {
        followService.unfollow(currentUser(req), followeeId);
    }
}