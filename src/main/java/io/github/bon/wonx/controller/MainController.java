package io.github.bon.wonx.controller;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
public class MainController {
  private final MainService mainService;

  public MainController(MainService mainService) {
    this.mainService = mainService;
  }

  // 메인 배너 콘텐츠 응답
  @GetMapping("/banner")
  public Video getMainBanner() {
    return mainService.getMainBannerVideo();

  }
}
