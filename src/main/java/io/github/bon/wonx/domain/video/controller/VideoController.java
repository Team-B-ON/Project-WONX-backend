package io.github.bon.wonx.domain.video.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
