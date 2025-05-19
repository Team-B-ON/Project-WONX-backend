package io.github.bon.wonx.domain.video.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.video.dto.HotVideoDto;
import io.github.bon.wonx.domain.video.dto.VideoDto;
import io.github.bon.wonx.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class VideoController {
  private final VideoService videoService;

  // 메인 배너
  @GetMapping("/banner")
  public ResponseEntity<VideoDto> getMainBanner() {
    return ResponseEntity.ok(videoService.getMainBannerVideo());
  }

  // 개봉 예정작
  @GetMapping("/upcoming")
  public ResponseEntity<List<VideoDto>> getUpcomingMovies() {
    return ResponseEntity.ok(videoService.getUpcomingMovies());
  }

  // 조회수 기반 인기작
  @GetMapping("/hot")
  public ResponseEntity<List<HotVideoDto>> getHotVideos(
      @RequestParam(defaultValue = "5") int count) {
    return ResponseEntity.ok(videoService.getHotVideos(count));
  }
}
