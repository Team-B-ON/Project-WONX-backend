package io.github.bon.wonx.domain.follow.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.follow.entity.Follow;
import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/follows")
public class AdminFollowController {

    private final FollowRepository followRepository;

    @GetMapping
    public ResponseEntity<List<AdminFollowDto>> getAllFollows() {
        List<Follow> follows = followRepository.findAll();
        List<AdminFollowDto> result = follows.stream()
                .map(AdminFollowDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
