package io.github.bon.wonx.domain.movies.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.entity.Like;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/likes")
public class AdminLikeController {

    private final LikeRepository likeRepository;

    @GetMapping
    public ResponseEntity<List<AdminLikeDto>> getAllLikes() {
        List<Like> likes = likeRepository.findAll();
        return ResponseEntity.ok(likes.stream().map(AdminLikeDto::fromEntity).toList());
    }
}
