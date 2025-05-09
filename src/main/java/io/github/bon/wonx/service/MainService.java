package io.github.bon.wonx.service;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.domain.video.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {
  private final VideoRepository videoRepository;

  public MainService(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  public Video getMainBannerVideo() {
    List<Video> topVideo = videoRepository.findTop1ByOrderByRatingDesc();
    return topVideo.isEmpty() ? null : topVideo.get(0);
  }
}
