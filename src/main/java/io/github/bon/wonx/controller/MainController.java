package io.github.bon.wonx.controller;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.service.MainService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/main")
public class MainController {
  private final MainService mainService;

  public MainController(MainService mainService) {
    this.mainService = mainService;
  }

  // 배너용: 평점 가장 높은 영상 1개
  @GetMapping("/banner")
  public Video getMainBanner() {
    return mainService.getMainBannerVideo();

  }

  // 최신작용: 오늘 ~ 7일 내 공개 예정 콘텐츠
  @GetMapping("/latest")
  public List<Video> getLatest() {
    return mainService.getUpcomingVideosWithinAWeek();
  }

}
