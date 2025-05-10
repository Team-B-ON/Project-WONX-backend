package io.github.bon.wonx.domain.video.controller;

import io.github.bon.wonx.domain.video.dto.BoxOfficeResponse;
import io.github.bon.wonx.domain.video.dto.TmdbUpcomingResponse;
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

  @GetMapping("/banner")
  public Video getMainBanner() {
    return videoService.getMainBannerVideo();

  }

  // 최신작용: 오늘 ~ 7일 내 공개 예정 콘텐츠
  // TMDB 상영 예정작 연동 업데이트
  @GetMapping("/latest")
  public TmdbUpcomingResponse getLatest() {
    return videoService.getUpcomingMoviesFromTmdb();
  }

  @GetMapping("/wonx-popular")
  public List<Video> getPopular(
      @RequestParam(defaultValue = "5") int count) {
    return videoService.getPopularVideos(count);
  }

  @GetMapping("/box-office")
  public List<BoxOfficeResponse> getBoxOffice() {
    return videoService.getBoxOfficeFromTmdb();
  }

}
