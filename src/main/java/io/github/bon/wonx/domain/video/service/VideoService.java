package io.github.bon.wonx.domain.video.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import io.github.bon.wonx.domain.tmdb.dto.TmdbMovieDto;
import io.github.bon.wonx.domain.video.dto.VideoDto;
import io.github.bon.wonx.domain.video.entity.Video;
import io.github.bon.wonx.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;

// tmdb에서 받은 영화 데이터를 db에 저장하는 기능
@Service
@RequiredArgsConstructor
public class VideoService {

  private final VideoRepository videoRepository;

  public void saveTmdbMovies(List<TmdbMovieDto> dtos) {
    for (int i = 0; i < dtos.size(); i++) {
      TmdbMovieDto dto = dtos.get(i);

      if (videoRepository.existsByTitleAndReleaseDate(dto.getTitle(), LocalDate.parse(dto.getReleaseDate()))) {
        continue; // 이미 있으면 저장 안 함
      }

      Video video = Video.builder()
          .title(dto.getTitle())
          .posterUrl("https://image.tmdb.org/t/p/w500" + dto.getPosterPath())
          .releaseDate(LocalDate.parse(dto.getReleaseDate()))
          .description(dto.getOverview())
          .boxOfficeRank(i + 1) // 인기도 순위
          .build();

      videoRepository.save(video);
    }
  }

  // 메인 배너
  public VideoDto getMainBannerVideo() {

    return videoRepository.findTop1ByOrderByBoxOfficeRankAsc()
        .map(VideoDto::from)
        .orElseThrow(() -> new RuntimeException("해당 영화를 찾을 수 없습니다."));
  }

  // 개봉 예정작
  public List<VideoDto> getUpcomingMovies() {
    LocalDate today = LocalDate.now();
    LocalDate weekLater = today.plusDays(7);

    return videoRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater).stream()
        .map(VideoDto::from)
        .toList();
  }
}
