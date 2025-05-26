package io.github.bon.wonx.domain.movies.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.LikeDto;
import io.github.bon.wonx.domain.movies.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies/{id}/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 좋아요 추가
    @PostMapping("")
    public ResponseEntity<LikeDto> create(@PathVariable UUID id, HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        LikeDto createdDto = likeService.create(userId, id);
        return ResponseEntity.ok(createdDto);
    }

    // 좋아요 삭제
    @DeleteMapping("")
    public ResponseEntity<LikeDto> delete(@PathVariable UUID id, HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        LikeDto deletedDto = likeService.delete(userId, id);
        return ResponseEntity.ok(deletedDto);
    }
}
