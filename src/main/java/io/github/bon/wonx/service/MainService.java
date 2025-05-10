package io.github.bon.wonx.service;

import io.github.bon.wonx.domain.video.Video;
import io.github.bon.wonx.domain.video.VideoRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MainService {
  private final VideoRepository videoRepository;

  public MainService(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  // 기존: 배너용 콘텐츠 조회
  public Video getMainBannerVideo() {
    List<Video> topVideo = videoRepository.findTop1ByOrderByRatingDesc();
    return topVideo.isEmpty() ? null : topVideo.get(0);
  }

  // 기존: 1주일 이내 개봉 콘텐츠
  public List<Video> getUpcomingVideosWithinAWeek() {
    LocalDate today = LocalDate.now(); // 오늘 기준
    LocalDate weekLater = today.plusDays(7); // 일주일 이내에 공개 예정인 영상 목록
    return videoRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater);
  }

  // 기존: 조회수 기준 인기 콘텐츠
  public List<Video> getPopularVideos(int count) {
    Pageable pageable = PageRequest.of(0, count);
    return videoRepository.findAllByOrderByViewCountDesc(pageable);
  }

  // 추가: 사용자 맞춤 추천 api (현재는 더미)
  public List<Video> getRecommendedVideo(UUID userId) {
    // 나중에 여기에 TMDB 연동 기반 로직을 추가할 예정
    // 1. userId 기반으로 사용자가 좋아요 누른 콘텐츠 or 평점 준 콘텐츠 조회
    // 2. 그 콘텐츠들의 TMBD ID로 연관 추천 요청 -> 유사한 콘텐츠 받아오기 (장르?)
    // 3. 응답 결과를 바탕으로 엔티티로 저장 혹은 추천 DTO로 반환

    // 지금은 일단 임시로 상위 5개 랜덤 반환
    Pageable pageable = PageRequest.of(0, 5); // 지금은 임시로 상위 5개 랜덤 반환
    return videoRepository.findAll(pageable).getContent();
  }
}
