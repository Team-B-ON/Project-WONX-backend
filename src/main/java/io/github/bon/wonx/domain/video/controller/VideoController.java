package io.github.bon.wonx.domain.video.controller;

import io.github.bon.wonx.domain.video.entity.Video;
import io.github.bon.wonx.domain.video.service.VideoService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/main")
public class VideoController {
  private final VideoService videoService;

  public VideoController(VideoService videoService) {
    this.videoService = videoService;
  }

  // 배너용: 평점 가장 높은 영상 1개
  @GetMapping("/banner")
  public Video getMainBanner() {
    return videoService.getMainBannerVideo();

  }

  // 최신작용: 오늘 ~ 7일 내 공개 예정 콘텐츠
  @GetMapping("/latest")
  public List<Video> getLatest() {
    return videoService.getUpcomingVideosWithinAWeek();
  }

  @GetMapping("/wonx-popular")
  public List<Video> getPopular(
      @RequestParam(defaultValue = "5") int count) {
    return videoService.getPopularVideos(count);
  }
}
