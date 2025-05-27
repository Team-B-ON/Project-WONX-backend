package io.github.bon.wonx.domain.profile.controller;

import io.github.bon.wonx.domain.profile.DTO.PublicProfileDto;
import io.github.bon.wonx.domain.profile.service.ProfilePageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfilePageService pageService;

    public ProfileController(ProfilePageService pageService) {
        this.pageService = pageService;
    }

    // 프로필 조회
    @GetMapping("/{userId}")
    public PublicProfileDto getPublicProfile(@PathVariable UUID userId) {
        return pageService.getProfileDetail(userId);
    }
}