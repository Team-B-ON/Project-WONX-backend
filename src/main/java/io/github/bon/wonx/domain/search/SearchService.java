package io.github.bon.wonx.domain.search;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.search.dto.MovieSearchDto;
import io.github.bon.wonx.domain.search.dto.ReviewSearchDto;
import io.github.bon.wonx.domain.search.dto.SearchResult;
import io.github.bon.wonx.domain.search.dto.SuggestionDto;
import io.github.bon.wonx.domain.search.dto.UserSearchDto;
import io.github.bon.wonx.domain.search.util.HangulUtils;
import io.github.bon.wonx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final MovieRepository movieRepository;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;

  public SearchResult searchAll(String keyword) {
    // 1. 키워드를 포함한 영화 제목으로 영화 검색
    List<MovieSearchDto> movies = movieRepository.findByTitleContainingIgnoreCase(keyword)
        .stream()
        .map(MovieSearchDto::from)
        .toList();

    // 2. 매칭된 영화들의 id 목록 추출
    List<UUID> movieIds = movies.stream()
        .map(MovieSearchDto::getId)
        .toList();

    // 3. 해당 영화에 대한 리뷰만 조회
    List<ReviewSearchDto> reviews = reviewRepository.findByMovieIds(movieIds)
        .stream()
        .map(ReviewSearchDto::from)
        .toList();

    // 4. 유저는 닉네임에 keyword 포함되었는지만 그대로
    List<UserSearchDto> users = userRepository.findByNicknameContainingIgnoreCase(keyword)
        .stream()
        .map(UserSearchDto::from)
        .toList();

  // 5. 결과 있으면 그대로 반환 (팩토리 메서드 활용)
  if (!movies.isEmpty() || !users.isEmpty()) {
    return SearchResult.of(movies, users, reviews);
  }

  // 6. 결과 없으면 유사 검색어 추천 포함하여 반환
  List<String> allTitles = movieRepository.findAllTitles(); // 직접 구현 필요
  List<String> similarKeywords = getSimilarKeywords(keyword, allTitles);

  List<SuggestionDto> suggestions = similarKeywords.stream()
      .map(k -> SuggestionDto.builder().keyword(k).build())
      .toList();

  return SearchResult.of(movies, users, reviews, suggestions);
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

}