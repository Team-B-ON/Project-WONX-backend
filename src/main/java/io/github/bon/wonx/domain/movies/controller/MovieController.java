package io.github.bon.wonx.domain.movies.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MovieLevel;
import io.github.bon.wonx.domain.movies.service.MovieService;
import io.github.bon.wonx.domain.user.PlanType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    // 영화 상세 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> details(@PathVariable UUID id) {
        MovieDto dto = movieService.details(id);
        return ResponseEntity.ok(dto);
    }

    // 함께 시청된 콘텐츠 조회
    @GetMapping("/{id}/related")
    public ResponseEntity<List<MovieDto>> relatedContents(@PathVariable UUID id) {
        List<MovieDto> dtos = movieService.relatedContents(id);
        return ResponseEntity.ok(dtos);
    }

    // 요금제 기반 Presigned URL 반환
    @GetMapping("/{id}/stream")
    public ResponseEntity<?> streamMovie(@PathVariable UUID id, HttpServletRequest request) {
        PlanType userPlan = (PlanType) request.getAttribute("userPlan");
        if (userPlan == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Movie movie = movieService.findEntity(id);
        MovieLevel requiredPlan = movie.getRequiredPlan();

        if (!isAccessible(userPlan, requiredPlan)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Upgrade your plan to watch this content.");
        }

        String presignedUrl = movieService.generatePresignedUrl(movie);
        return ResponseEntity.ok(presignedUrl);
    }

    private boolean isAccessible(PlanType userPlan, MovieLevel requiredPlan) {
        return switch (requiredPlan) {
            case BASIC -> true;
            case STANDARD -> userPlan == PlanType.STANDARD || userPlan == PlanType.PREMIUM;
            case PREMIUM -> userPlan == PlanType.PREMIUM;
        };
    }
}
