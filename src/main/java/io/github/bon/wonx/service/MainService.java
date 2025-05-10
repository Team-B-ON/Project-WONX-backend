package io.github.bon.wonx.service;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.domain.video.VideoRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

  public List<Video> getUpcomingVideosWithinAWeek() {
    LocalDate today = LocalDate.now(); // 오늘 기준
    LocalDate weekLater = today.plusDays(7); // 일주일 이내에 공개 예정인 영상 목록
    return videoRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater);
  }

  public List<Video> getPopularVideos(int count) {
    Pageable pageable = PageRequest.of(0, count);
    return videoRepository.findAllByOrderByViewCountDesc(pageable);
  }
}
