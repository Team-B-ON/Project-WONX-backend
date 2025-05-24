package io.github.bon.wonx.home;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MovieLevel;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class HotMoviesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private LikeRepository likesRepository; // ← 이거 주입

  @BeforeEach
  void setUp() {
    likesRepository.deleteAll(); // ← 이거 먼저!
    movieRepository.deleteAll();

    // 조회수 높은 순으로 여러 개 삽입
    for (int i = 1; i <= 6; i++) {
      movieRepository.save(Movie.builder()
          .id(UUID.randomUUID())
          .title("인기 영화 " + i)
          .description("조회수 테스트용")
          .posterUrl("https://image.test/popular" + i + ".jpg")
          .releaseDate(LocalDate.of(2024, 1, 1))
          .viewCount(1000 - i * 100) // 900, 800, ...
          .requiredPlan(MovieLevel.BASIC)
          .build());
    }
  }

  @Test
  @DisplayName("인기 영화 조회: 기본 count(5)")
  void testHotMoviesDefaultCount() throws Exception {
    mockMvc.perform(get("/api/home/hot-movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(5))
        .andExpect(jsonPath("$[0].title").value("인기 영화 1")); // 조회수 900
  }

  @Test
  @DisplayName("인기 영화 조회: count=3 요청 시 3개만 반환")
  void testHotMoviesWithCountParam() throws Exception {
    mockMvc.perform(get("/api/home/hot-movies?count=3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[2].title").value("인기 영화 3")); // 700
  }
}
