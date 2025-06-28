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
public class ReviewCountControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private HotTalkRepository hotTalkRepository;
  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private LikeRepository likesRepository;

  @BeforeEach
  void setUp() {
    likesRepository.deleteAll();
    hotTalkRepository.deleteAll();
    movieRepository.deleteAll();

    Movie movie = movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("누적리뷰용 영화")
        .posterUrl("https://img.review/test.jpg")
        .releaseDate(LocalDate.of(2024, 1, 1))
        .requiredPlan(MovieLevel.BASIC)
        .build());

    for (int i = 1; i <= 3; i++) {
      hotTalkRepository.save(HotTalk.builder()
          .content("리뷰 " + i)
          .viewCount(100 + i)
          .createdAt(LocalDateTime.now().minusDays(i))
          .movie(movie)
          .build());
    }
  }

  @Test
  @DisplayName("누적 리뷰 수 조회 API: 등록된 리뷰 수 반환")
  void getReviewCount() throws Exception {
    mockMvc.perform(get("/api/home/review-count"))
        .andExpect(status().isOk())
        .andExpect(content().string("3"));
  }
}
