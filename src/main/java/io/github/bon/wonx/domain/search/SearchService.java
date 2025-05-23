package io.github.bon.wonx.domain.search;

import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.search.dto.MovieSearchDto;
import io.github.bon.wonx.domain.search.dto.SearchResult;
import io.github.bon.wonx.domain.search.dto.SuggestionDto;
import io.github.bon.wonx.domain.search.dto.UserSearchDto;
import io.github.bon.wonx.domain.search.util.HangulUtils;
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

    // 결과가 있다면 그냥 반환
    if (!movies.isEmpty() || !users.isEmpty()) {
      return SearchResult.builder()
          .movies(movies)
          .users(users)
          .build();
    }

    // 검색 결과가 없을 경우 → 유사 키워드 추천
    List<String> allTitles = movieRepository.findAllTitles(); // 이 메서드는 추가 구현 필요
    List<String> similarKeywords = getSimilarKeywords(keyword, allTitles);

    List<SuggestionDto> suggestions = similarKeywords.stream()
        .map(k -> SuggestionDto.builder().keyword(k).build())
        .toList();

    return SearchResult.builder()
        .movies(movies)
        .users(users)
        .suggestions(suggestions)
        .build();
  }

  private List<String> getSimilarKeywords(String keyword, List<String> candidates) {
    String disKeyword = HangulUtils.disassemble(keyword.toLowerCase());
    String keywordChosung = HangulUtils.extractChosung(keyword.toLowerCase());

    return candidates.stream()
        .filter(candidate -> {
          String disCandidate = HangulUtils.disassemble(candidate.toLowerCase());
          String candidateChosung = HangulUtils.extractChosung(candidate.toLowerCase());

          int distance = levenshteinDistance(disKeyword, disCandidate);

          // 거리 또는 초성 일치 중 하나라도 만족하면 통과
          return disCandidate.contains(disKeyword)
              || distance <= 3
              || candidateChosung.contains(keywordChosung);
        })
        .limit(5)
        .toList();
  }

  private int levenshteinDistance(String a, String b) {
    int[][] dp = new int[a.length() + 1][b.length() + 1];
    for (int i = 0; i <= a.length(); i++)
      dp[i][0] = i;
    for (int j = 0; j <= b.length(); j++)
      dp[0][j] = j;

    for (int i = 1; i <= a.length(); i++) {
      for (int j = 1; j <= b.length(); j++) {
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1];
        } else {
          dp[i][j] = Math.min(dp[i - 1][j - 1],
              Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
        }
      }
    }
    return dp[a.length()][b.length()];
  }
  // List<ReviewSearchDto> reviews =
  // reviewRepository.findByContentContaining(keyword)
  // .stream()
  // .map(ReviewSearchDto::from)
  // .toList();

  // return new SearchResult(movies, users, reviews);

}
