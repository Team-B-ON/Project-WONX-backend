package io.github.bon.wonx.domain.home;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HomeServiceTest {
  @Autowired
  private HomeService homeService;

  @Autowired
  private MovieRepository movieRepository;

  @BeforeEach
  void setUp() {
    movieRepository.deleteAll();

    // 배너용 영화 (boxOfficeRank = 1)
    movieRepository.save(Movie.builder()
        .title("1위 영화")
        .description("배너용 영화")
        .posterUrl("https://example.com/banner.jpg")
        .releaseDate(LocalDate.of(2024, 1, 1))
        .boxOfficeRank(1)
        .viewCount(0)
        .build());

    // 핫 콘텐츠 테스트용
    movieRepository.save(Movie.builder()
        .title("조회수 200")
        .posterUrl("https://example.com/200.jpg")
        .viewCount(200)
        .releaseDate(LocalDate.now().minusDays(10))
        .boxOfficeRank(99) // boxOffice에서는 밀어버림
        .build());

    movieRepository.save(Movie.builder()
        .title("조회수 100")
        .posterUrl("https://example.com/100.jpg")
        .viewCount(100)
        .releaseDate(LocalDate.now().minusDays(9)) // 과거로
        .boxOfficeRank(100)
        .build());

    movieRepository.save(Movie.builder()
        .title("조회수 10")
        .posterUrl("https://example.com/10.jpg")
        .viewCount(10)
        .releaseDate(LocalDate.now().minusDays(8)) // 과거로
        .boxOfficeRank(101)
        .build());

    // 개봉 예정작 테스트용
    movieRepository.save(Movie.builder()
        .title("오늘 개봉")
        .releaseDate(LocalDate.now())
        .boxOfficeRank(200)
        .viewCount(0)
        .build());

    movieRepository.save(Movie.builder()
        .title("7일 후 개봉")
        .releaseDate(LocalDate.now().plusDays(7))
        .boxOfficeRank(201)
        .viewCount(0)
        .build());

    movieRepository.save(Movie.builder()
        .title("8일 후 개봉")
        .releaseDate(LocalDate.now().plusDays(8)) // 이건 테스트에서 제외돼야 함
        .boxOfficeRank(202)
        .viewCount(0)
        .build());
  }

  @Test
  void getBannerMovie는_박스오피스1위_영화를_반환한다() {
    MovieDto result = homeService.getBannerMovie();

    assertThat(result.getTitle()).isEqualTo("1위 영화");
    assertThat(result.getDescription()).isEqualTo("배너용 영화");
  }

  @Test
  void getHotMovies는_조회수_내림차순으로_정렬해서_반환한다() {
    List<HotMovieDto> result = homeService.getHotMovies(3);

    assertThat(result).hasSize(3);
    assertThat(result.get(0).getTitle()).isEqualTo("조회수 200");
    assertThat(result.get(1).getTitle()).isEqualTo("조회수 100");
    assertThat(result.get(2).getTitle()).isEqualTo("조회수 10");
  }

  @Test
  void getUpcomingMovies는_오늘부터_7일이내_영화만_반환한다() {
    List<MovieDto> result = homeService.getUpcomingMovies();

    assertThat(result).hasSize(2);
    assertThat(result).extracting("title")
        .containsExactlyInAnyOrder("오늘 개봉", "7일 후 개봉");
  }
}
