package io.github.bon.wonx.home;

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
import java.util.UUID;
import java.util.stream.IntStream;

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
public class BoxOfficeControllerTest {

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
    hotTalkRepository.deleteAll(); // 참조하는 애 먼저 삭제
    movieRepository.deleteAll(); // 그 다음 삭제

    // 박스오피스 순위 1~12까지 넣고 상위 10개만 응답되도록 테스트
    IntStream.rangeClosed(1, 12).forEach(rank -> {
      movieRepository.save(Movie.builder()
          .id(UUID.randomUUID())
          .title("박스오피스 영화 " + rank)
          .posterUrl("https://poster.example.com/" + rank)
          .releaseDate(LocalDate.of(2024, 1, rank))
          .requiredPlan(MovieLevel.BASIC)
          .boxOfficeRank(rank)
          .build());
    });
  }

  @Test
  @DisplayName("박스오피스 API: 상위 10개만 오름차순으로 반환")
  void testBoxOfficeMovies() throws Exception {
    mockMvc.perform(get("/api/home/box-office"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(10))
        .andExpect(jsonPath("$[0].title").value("박스오피스 영화 1"))
        .andExpect(jsonPath("$[9].title").value("박스오피스 영화 10"))
        .andExpect(jsonPath("$[0].boxOfficeRank").value(1));
  }
}
