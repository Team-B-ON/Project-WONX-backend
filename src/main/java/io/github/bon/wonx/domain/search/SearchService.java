package io.github.bon.wonx.domain.search;

import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.search.dto.MovieSearchDto;
import io.github.bon.wonx.domain.search.dto.SearchResult;
import io.github.bon.wonx.domain.search.dto.UserSearchDto;
import io.github.bon.wonx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final MovieRepository movieRepository;
  private final UserRepository userRepository;
  // private final ReviewRepository reviewRepository;

  public SearchResult searchAll(String keyword) {
    List<MovieSearchDto> movies = movieRepository.findByTitleContainingIgnoreCase(keyword)
        .stream()
        .map(MovieSearchDto::from)
        .toList();

    List<UserSearchDto> users = userRepository.findByNicknameContainingIgnoreCase(keyword)
        .stream()
        .map(UserSearchDto::from)
        .toList();

    return SearchResult.builder()
        .movies(movies)
        .users(users)
        .build();

    // List<ReviewSearchDto> reviews =
    // reviewRepository.findByContentContaining(keyword)
    // .stream()
    // .map(ReviewSearchDto::from)
    // .toList();

    // return new SearchResult(movies, users, reviews);
  }
}
