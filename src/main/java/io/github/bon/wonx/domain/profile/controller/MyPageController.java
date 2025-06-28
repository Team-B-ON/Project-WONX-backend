package io.github.bon.wonx.domain.profile.controller;

import io.github.bon.wonx.domain.follow.service.FollowService;
import io.github.bon.wonx.domain.profile.dto.ProfileUpdateRequest;
import io.github.bon.wonx.domain.profile.dto.PublicProfileDto;
import io.github.bon.wonx.domain.profile.service.ProfilePageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

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
        // 더미 사용
        UUID me = (UUID) req.getAttribute("userId");
        if (me == null) {
            me = UUID.fromString("11111111-1111-1111-1111-111111111111");
        }
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