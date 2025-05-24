package io.github.bon.wonx.search;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MovieLevel;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.reviews.Review;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
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
public class SearchServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @BeforeEach
  void setUp() {
    // 데이터 정리
    reviewRepository.deleteAll();
    userRepository.deleteAll();
    movieRepository.deleteAll();

    // 영화 저장
    Movie movie = Movie.builder()
        .id(UUID.randomUUID())
        .title("인셉션")
        .posterUrl("/inception.jpg")
        .releaseDate(LocalDate.of(2010, 7, 21))
        .requiredPlan(MovieLevel.BASIC)
        .build();
    movieRepository.save(movie);

    // 유저 저장
    User user = User.builder()
        .email("inception@dream.com")
        .nickname("인셉션광팬")
        .profileImageUrl("/inceptionFan.png")
        .build();
    userRepository.save(user);

    // 리뷰 저장 (→ 영화와 연결)
    Review review = new Review(user, movie, 9, "인셉션 진짜 대박", false);
    reviewRepository.save(review);
  }

  @Test
  @DisplayName("검색어로 매칭된 영화, 유저, 해당 영화의 리뷰를 반환한다")
  void searchAllTest() throws Exception {
    mockMvc.perform(get("/api/search")
        .param("keyword", "인셉션")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.movies[0].title").value("인셉션"))
        .andExpect(jsonPath("$.users[0].nickname").value("인셉션광팬"))
        .andExpect(jsonPath("$.reviews[0].content").value("인셉션 진짜 대박"));
  }
}
