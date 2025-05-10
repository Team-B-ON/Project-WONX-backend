package io.github.bon.wonx.controller;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.service.MainService;

import java.util.List;
import java.util.UUID;

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

  @GetMapping("/banner")
  public Video getMainBanner() {
    return mainService.getMainBannerVideo();

  }

  @GetMapping("/latest")
  public List<Video> getLatest() {
    return mainService.getUpcomingVideosWithinAWeek();
  }

  @GetMapping("/wonx-popular")
  public List<Video> getPopular(
      @RequestParam(defaultValue = "5") int count) {
    return mainService.getPopularVideos(count);
  }

  // 사용자 맞춤 추천 api
  @GetMapping("/recommend")
  public List<Video> getRecommend(@RequestParam UUID userId) {
    // 클라이언트가 userId를 쿼리 파라미터로 전달해야 함
    return mainService.getRecommendedVideo(userId);
  }

}
