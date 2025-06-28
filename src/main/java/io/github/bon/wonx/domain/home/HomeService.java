package io.github.bon.wonx.domain.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.home.dto.BoxOfficeDto;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.home.dto.HotTalkDto;
import io.github.bon.wonx.domain.home.dto.RecommendDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Like;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
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

  // 사용자 기반 콘텐츠를 추천하기 위한 레포지토리
  private final LikeRepository likeRepository;

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

  // 전체 누적 리뷰 수 반환
  public long getTotalReviewCount() {
    return talkRepository.count();
  }

  // 사용자 맞춤 추천 (좋아요 기반)
  public List<RecommendDto> getRecommendedMovies(UUID userId) {

    // userId를 기준으로 사용자가 좋아요한 영화 가져오기
    List<Like> likes = likeRepository.findByUserId(userId);
    if (likes.isEmpty()) {
      return List.of(); // 좋아요한 영화 없으면 빈 리스트 반환
    }

    // 좋아요한 영화 리스트
    List<Movie> likedMovies = likes.stream()
        .map(Like::getMovie)
        .toList();

    // 선호 장르 추출
    Set<Genre> preferredGenres = likedMovies.stream()
        .flatMap(movie -> movie.getGenres().stream())
        .collect(Collectors.toSet());

    // 장르 정보 없을 경우 → 랜덤 추천 (조회수 상위 몇 개 중에서 무작위)
    if (preferredGenres.isEmpty()) {
      List<Movie> popularMovies = movieRepository.findAllByOrderByViewCountDesc(Pageable.ofSize(20)); // 인기 상위 20개
      Collections.shuffle(popularMovies); // 랜덤 섞기
      return popularMovies.stream()
          .limit(10) // 10개만 추천
          .map(movie -> new RecommendDto(
              movie.getTitle(),
              movie.getPosterUrl(),
              movie.getBoxOfficeRank()))
          .toList();
    }

    // 추천 제외용: 이미 좋아요한 영화 ID
    List<UUID> excludeIds = likedMovies.stream()
        .map(Movie::getId)
        .toList();

    // 추천 영화 조회
    List<Movie> recommended = movieRepository
        .findDistinctByGenresInAndIdNotInOrderByViewCountDesc(new ArrayList<>(preferredGenres), excludeIds);

    // DTO 변환
    return recommended.stream()
        .map(movie -> new RecommendDto(
            movie.getTitle(),
            movie.getPosterUrl(),
            movie.getBoxOfficeRank()))
        .toList();
  }

}