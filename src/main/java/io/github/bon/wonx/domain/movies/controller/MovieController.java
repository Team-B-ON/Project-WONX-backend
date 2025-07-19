package io.github.bon.wonx.domain.movies.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.MovieDetailDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.dto.RelatedMovieResponse;
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
    public ResponseEntity<MovieDetailDto> details(
        @PathVariable UUID id,
        @RequestAttribute(value = "userId", required = false) UUID userId
    ) {
        MovieDetailDto dto = movieService.details(id, userId);
        return ResponseEntity.ok(dto);
    }

    // 함께 시청된 콘텐츠 조회
    @GetMapping("/{id}/related")
    public ResponseEntity<RelatedMovieResponse> relatedContents(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "6") int limit
    ) {
        RelatedMovieResponse response = movieService.relatedContents(id, offset, limit);
        return ResponseEntity.ok(response);
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

    @GetMapping
    public ResponseEntity<List<MovieDto>> findByIds(
            @RequestParam(required = false) String ids
    ) {
        if (ids == null || ids.isBlank()) {
            // ids 파라미터가 없으면 빈 배열 리턴하거나, 전체 목록을 리턴하도록 변경해도 되고요.
            return ResponseEntity.ok(List.of());
        }
        List<UUID> uuidList = Arrays.stream(ids.split(","))
                .map(UUID::fromString)
                .toList();
        return ResponseEntity.ok(movieService.findByIds(uuidList));
    }
}
