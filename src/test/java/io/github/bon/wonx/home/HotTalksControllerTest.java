package io.github.bon.wonx.home;

import io.github.bon.wonx.domain.home.HotTalk;
import io.github.bon.wonx.domain.home.HotTalkRepository;
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
import java.time.LocalDateTime;
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
public class HotTalksControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private HotTalkRepository hotTalkRepository;
  @Autowired
  private LikeRepository likesRepository; // ← 이거 주입

  @BeforeEach
  void setUp() {
    likesRepository.deleteAll(); // ← 이거 먼저!
    hotTalkRepository.deleteAll();
    movieRepository.deleteAll();

    Movie movie = movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("리뷰 대상 영화")
        .description("설명")
        .posterUrl("https://poster.url/sample.jpg")
        .releaseDate(LocalDate.now())
        .requiredPlan(MovieLevel.BASIC)
        .viewCount(100)
        .build());

    for (int i = 1; i <= 5; i++) {
      hotTalkRepository.save(HotTalk.builder()
          .content("리뷰 내용 " + i)
          .viewCount(100 + i * 10) // 110, 120, 130, ...
          .createdAt(LocalDateTime.now().minusDays(i))
          .movie(movie)
          .build());
    }
  }

  @Test
  @DisplayName("지금 뜨는 리뷰 API: 상위 3개 리뷰 반환")
  void getHotTalksTest() throws Exception {
    mockMvc.perform(get("/api/home/hot-talks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].talkContent").value("리뷰 내용 5")) // 조회수 가장 높은 순
        .andExpect(jsonPath("$[1].talkContent").value("리뷰 내용 4"))
        .andExpect(jsonPath("$[2].talkContent").value("리뷰 내용 3"));
  }
}
