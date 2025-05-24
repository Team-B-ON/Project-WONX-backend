package io.github.bon.wonx.home;

import io.github.bon.wonx.domain.home.HotTalkRepository;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Like;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MovieLevel;
import io.github.bon.wonx.domain.movies.repository.GenreRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
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
public class RecommendControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private LikeRepository likeRepository;
  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private HotTalkRepository hotTalkRepository;

  private UUID userId;

  @BeforeEach
  void setUp() {
    likeRepository.deleteAll();
    hotTalkRepository.deleteAll();
    movieRepository.deleteAll();
    userRepository.deleteAll();
    genreRepository.deleteAll();

    Genre action = genreRepository.save(new Genre("액션"));

    // User ID 자동 생성 (Detached 방지)
    User user = userRepository.save(User.builder()
        .email("test@example.com")
        .nickname("테스터")
        .build());

    this.userId = user.getId();

    Movie liked = movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("내가 좋아요한 영화")
        .posterUrl("https://poster/liked.jpg")
        .releaseDate(LocalDate.now())
        .genres(List.of(action))
        .requiredPlan(MovieLevel.BASIC)
        .build());

    likeRepository.save(new Like(user, liked));

    movieRepository.save(Movie.builder()
        .id(UUID.randomUUID())
        .title("추천 영화")
        .posterUrl("https://poster/recommend.jpg")
        .releaseDate(LocalDate.now())
        .genres(List.of(action))
        .requiredPlan(MovieLevel.BASIC)
        .boxOfficeRank(1)
        .viewCount(999)
        .build());
  }

  @Test
  @DisplayName("추천 API: 좋아요 기반 장르로 추천 영화 반환")
  void recommendMoviesTest() throws Exception {
    mockMvc.perform(get("/api/home/recommend")
        .requestAttr("userId", userId)) // 필터 통과 시뮬레이션
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value("추천 영화"));
  }
}
