package io.github.bon.wonx.domain.video.service;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.bon.wonx.domain.video.dto.HotVideoDto;
import io.github.bon.wonx.domain.video.dto.VideoDto;
import io.github.bon.wonx.domain.video.entity.Video;
import io.github.bon.wonx.domain.video.repository.VideoRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VideoServiceTest {

  @Autowired
  private VideoService videoService;

  @Autowired
  private VideoRepository videoRepository;

  @BeforeEach
  void setUp() {
    // DB 초기화
    videoRepository.deleteAll();

    // 2위짜리 임시 영상
    videoRepository.save(Video.builder()
        .title("하위 영화")
        .description("하위 설명")
        .posterUrl("https://example.com/poster2.jpg")
        .releaseDate(LocalDate.of(2024, 1, 1))
        .boxOfficeRank(2)
        .build());

    // 1위짜리 영상
    videoRepository.save(Video.builder()
        .title("1위 영화")
        .description("최고의 영화!")
        .posterUrl("https://example.com/poster1.jpg")
        .releaseDate(LocalDate.of(2024, 2, 1))
        .boxOfficeRank(1)
        .build());

    videoRepository.save(Video.builder()
        .title("조회수 10")
        .posterUrl("https://example.com/10.jpg")
        .viewCount(10)
        .releaseDate(LocalDate.now())
        .build());

    videoRepository.save(Video.builder()
        .title("조회수 300")
        .posterUrl("https://example.com/300.jpg")
        .viewCount(300)
        .releaseDate(LocalDate.now())
        .build());

    videoRepository.save(Video.builder()
        .title("조회수 150")
        .posterUrl("https://example.com/150.jpg")
        .viewCount(150)
        .releaseDate(LocalDate.now())
        .build());
  }

  @Test
  void getMainBannerVideo는_박스오피스1위를_반환한다() {
    // when
    VideoDto result = videoService.getMainBannerVideo();

    // then
    assertThat(result.getTitle()).isEqualTo("1위 영화");
    assertThat(result.getDescription()).isEqualTo("최고의 영화!");
    assertThat(result.getPosterUrl()).isEqualTo("https://example.com/poster1.jpg");
  }

  @Test
  void getUpcomingMoviesWithinAWeek는_오늘부터_7일이내_개봉작만_반환한다() {
    // given
    LocalDate today = LocalDate.now();
    videoRepository.save(Video.builder().title("오늘 개봉").releaseDate(today).build());
    videoRepository.save(Video.builder().title("3일 후 개봉").releaseDate(today.plusDays(3)).build());
    videoRepository.save(Video.builder().title("7일 후 개봉").releaseDate(today.plusDays(7)).build());
    videoRepository.save(Video.builder().title("8일 후 개봉").releaseDate(today.plusDays(8)).build());
    videoRepository.save(Video.builder().title("어제 개봉").releaseDate(today.minusDays(1)).build());

    // when
    var results = videoService.getUpcomingMovies();

    // then
    assertThat(results).hasSize(3);
    assertThat(results).extracting("title")
        .containsExactlyInAnyOrder("오늘 개봉", "3일 후 개봉", "7일 후 개봉");
  }

  @Test
  void getHotVideos는_조회수_내림차순으로_정렬된_영상들을_반환한다() {
    // when
    List<HotVideoDto> results = videoService.getHotVideos(3);

    // then
    assertThat(results).hasSize(3);
    assertThat(results.get(0).getTitle()).isEqualTo("조회수 300");
    assertThat(results.get(1).getTitle()).isEqualTo("조회수 150");
    assertThat(results.get(2).getTitle()).isEqualTo("조회수 10");
  }

}
