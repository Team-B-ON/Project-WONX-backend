package io.github.bon.wonx.domain.profile.controller;

import io.github.bon.wonx.domain.profile.dto.PublicProfileDto;
import io.github.bon.wonx.domain.profile.service.ProfilePageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfilePageService pageService;

    @GetMapping("/{userId}")
    public PublicProfileDto getPublicProfile(@PathVariable UUID userId,
                                             HttpServletRequest req) {
        UUID viewer = (UUID) req.getAttribute("userId"); // null 허용
        return pageService.getProfileDetail(userId, viewer);
    }
}
