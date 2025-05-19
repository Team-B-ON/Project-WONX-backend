package io.github.bon.wonx.domain.movies.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.service.MovieService;
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
}