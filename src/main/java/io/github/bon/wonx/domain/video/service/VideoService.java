package io.github.bon.wonx.domain.video.service;

import io.github.bon.wonx.domain.video.dto.TmdbUpcomingResponse;
import io.github.bon.wonx.domain.video.entity.Video;
import io.github.bon.wonx.domain.video.repository.VideoRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;

@Service
public class VideoService {
  private final VideoRepository videoRepository;
  private final RestTemplate restTemplate;

  // properties 파일에 저장해둔 키를 가져와 사용
  @Value("${tmdb.api-key}")
  private String apiKey;

  public VideoService(VideoRepository videoRepository, RestTemplate restTemplate) {
    this.videoRepository = videoRepository;
    this.restTemplate = restTemplate;
  }

  public Video getMainBannerVideo() {
    List<Video> topVideo = videoRepository.findTop1ByOrderByRatingDesc();
    return topVideo.isEmpty() ? null : topVideo.get(0);
  }

  public List<Video> getPopularVideos(int count) {
    Pageable pageable = PageRequest.of(0, count);
    return videoRepository.findAllByOrderByViewCountDesc(pageable);
  }

  public List<Video> getUpcomingVideosWithinAWeek() {
    LocalDate today = LocalDate.now(); // 오늘 기준
    LocalDate weekLater = today.plusDays(7); // 일주일 이내에 공개 예정인 영상 목록
    return videoRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater);
  }

  // TMDB 연동: 상영 예정작 가져오기
  public TmdbUpcomingResponse getUpcomingMoviesFromTmdb() {
    String url = "https://api.themoviedb.org/3/movie/upcoming"
        + "?api_key=" + apiKey
        + "&language=ko-KR"
        + "&region=KR";

    return restTemplate.getForObject(url, TmdbUpcomingResponse.class);
  }
}
