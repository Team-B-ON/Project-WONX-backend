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
    "JWT_ACCESS_EXPIRATION=3600000", // 1시간짜리
    "JWT_REFRESH_EXPIRATION=604800000", // 7일짜리
    "GMAIL_USERNAME=dummy@gmail.com", // <- 요거 추가
    "GMAIL_PASSWORD=dummypassword", // <- 보통 같이 필요함
    "AWS_ACCESS_KEY_ID=dummykey",
    "AWS_SECRET_ACCESS_KEY=dummysecret",
    "cloud.aws.region.static=ap-northeast-2",
    "cloud.aws.stack.auto=false",
    "cloud.aws.s3.bucket=test-bucket"
})
public class BannerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private HotTalkRepository hotTalkRepository;

  @Autowired
  private LikeRepository likesRepository; // ← 이거 주입

  @BeforeEach
  void setup() {
    likesRepository.deleteAll(); // ← 이거 먼저!
    hotTalkRepository.deleteAll();
    movieRepository.deleteAll();
    movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("1위 배너용 영화")
        .description("설명도 있음")
        .requiredPlan(MovieLevel.BASIC)
        .posterUrl("https://image.banner/test.jpg")
        .releaseDate(LocalDate.of(2024, 1, 1))
        .boxOfficeRank(1)
        .build());
  }

  @Test
  @DisplayName("배너 API: 박스오피스 1위 영화 반환 확인")
  void getBannerTest() throws Exception {
    mockMvc.perform(get("/api/home/banner"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("1위 배너용 영화"))
        .andExpect(jsonPath("$.description").value("설명도 있음"));
  }
}
