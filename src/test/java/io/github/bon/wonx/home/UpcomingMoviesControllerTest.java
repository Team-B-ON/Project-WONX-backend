package io.github.bon.wonx.home;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.github.bon.wonx.domain.home.HotTalkRepository;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MovieLevel;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "JWT_SECRET=12345678901234567890123456789012",
    "JWT_ACCESS_EXPIRATION=3600000",
    "JWT_REFRESH_EXPIRATION=604800000",
    "GMAIL_USERNAME=dummy@gmail.com",
    "GMAIL_PASSWORD=dummypassword",
    "AWS_ACCESS_KEY_ID=dummykey",
    "AWS_SECRET_ACCESS_KEY=dummysecret",
    "cloud.aws.region.static=ap-northeast-2",
    "cloud.aws.stack.auto=false",
    "cloud.aws.s3.bucket=test-bucket"
})
public class UpcomingMoviesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private HotTalkRepository hotTalkRepository;

  @Autowired
  private LikeRepository likesRepository;

  @BeforeEach
  void setup() {
    likesRepository.deleteAll();
    hotTalkRepository.deleteAll();
    movieRepository.deleteAll();

    movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("예정작 테스트")
        .description("곧 개봉하는 영화입니다")
        .releaseDate(LocalDate.now().plusDays(3)) // 7일 이내
        .posterUrl("https://image.test/poster.jpg")
        .requiredPlan(MovieLevel.BASIC)
        .build());

    movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("이미 개봉한 영화")
        .description("과거 영화")
        .releaseDate(LocalDate.now().minusDays(1)) // 과거
        .posterUrl("https://image.test/poster2.jpg")
        .requiredPlan(MovieLevel.BASIC)
        .build());
  }

  @Test
  @DisplayName("상영 예정작 API: 오늘~7일 이내 개봉 영화만 반환")
  void getUpcomingMoviesTest() throws Exception {
    mockMvc.perform(get("/api/home/upcoming"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value("예정작 테스트"));
  }
}
