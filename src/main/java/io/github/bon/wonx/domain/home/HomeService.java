package io.github.bon.wonx.domain.home;

import java.util.List;
import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.home.dto.BoxOfficeDto;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.home.dto.HotTalkDto;
import io.github.bon.wonx.domain.home.repository.HotTalkRepository;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.movies.service.MovieService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
  // tmdb에서 가져온 영화 데이터를 다루는 서비스
  private final MovieService movieService;
  // 지금 뜨는 리뷰(HotTalk) 데이터를 조회하기 위한 레포지토리
  private final HotTalkRepository talkRepository;
  // 박스오피스 top10 데이터를 조회하기 위한 레포지토리
  private final MovieRepository movieRepository;

  public MovieDto getBannerMovie() {
    return movieService.getBannerMovie();
  }

  public List<MovieDto> getUpcomingMovies() {
    return movieService.getUpcomingMovies();
  }

  public List<HotMovieDto> getHotMovies(int count) {
    return movieService.getHotMovies(count);
  }

  // 지금 뜨는 리뷰(HotTalk) 상위 3개를 조회수 + 최신순 기준으로 반환
  public List<HotTalkDto> getHotTalks() {
    return talkRepository.findTop3ByOrderByViewCountDescCreatedAtDesc().stream()
        .map(talk -> new HotTalkDto(
            talk.getMovie().getTitle(), // 영화 제목
            talk.getMovie().getPosterUrl(), // 영화 썸네일
            talk.getContent(), // 리뷰 내용
            talk.getViewCount(), // 조회수
            talk.getCreatedAt())) // 작성 시간
        .toList();
  }

  // 박스오피스 top10 조회 기능
  public List<BoxOfficeDto> getBoxOfficeMovies() {
    return movieRepository.findTop10ByBoxOfficeRankIsNotNullOrderByBoxOfficeRankAsc().stream()
        .map(movie -> new BoxOfficeDto(
            movie.getTitle(),
            movie.getPosterUrl(),
            movie.getBoxOfficeRank()))
        .toList();
  }
}
