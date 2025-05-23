package io.github.bon.wonx.domain.tmdb;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import io.github.bon.wonx.domain.tmdb.dto.TmdbResponseDto;
import io.github.bon.wonx.global.config.DotenvConfig;
import io.github.bon.wonx.domain.tmdb.dto.TmdbMovieDto;
import lombok.RequiredArgsConstructor;

// tmdb에서 영화를 받아오는 작업 -> 우리 db에 저장하는 작업
@Component
@RequiredArgsConstructor
public class TmdbClient {

  // 임시 생성
  private final RestTemplate restTemplate = new RestTemplate();
  private final String apiKey = DotenvConfig.get("TMDB_API_KEY");

  // tmdb에서 박스 오피스 순위를 조회하는 메서드
  public List<TmdbMovieDto> fetchBoxOfficeMovies() {
    List<TmdbMovieDto> allResults = new ArrayList<>();

    // 일단 1-2 페이지 분량만 받아옴 -> 추후 상의 필요
    for (int page = 1; page <= 2; page++) {
      String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=ko-KR&page=" + page;
      TmdbResponseDto response = restTemplate.getForObject(url, TmdbResponseDto.class);
      allResults.addAll(response.getResults());
    }

    return allResults;
  }
}
