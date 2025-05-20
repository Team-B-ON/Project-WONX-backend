package io.github.bon.wonx.domain.home;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import io.github.bon.wonx.domain.home.repository.HotTalkRepository;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchServiceTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private HotTalkRepository hotTalkRepository;

  @BeforeEach
  void setUp() {
    // FK 의존성 있는 테이블 먼저 삭제
    hotTalkRepository.deleteAll();
    userRepository.deleteAll();
    movieRepository.deleteAll();

    movieRepository.save(Movie.builder()
        .title("인셉션")
        .posterUrl("/inception.jpg")
        .releaseDate(LocalDate.of(2010, 7, 21))
        .build());

    userRepository.save(User.builder()
        .email("inception@dream.com")
        .nickname("인셉션광팬")
        .profileImageUrl("/inceptionFan.png")
        .build());
  }

  @Test
  @DisplayName("검색어가 포함된 영화와 유저를 검색한다")
  void searchTest() throws Exception {
    mockMvc.perform(get("/api/search")
        .param("keyword", "인셉션")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.movies[0].title").value("인셉션"))
        .andExpect(jsonPath("$.users[0].nickname").value("인셉션광팬"));
  }
}
