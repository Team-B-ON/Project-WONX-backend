package io.github.bon.wonx.domain.movies.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.LikeDto;
import io.github.bon.wonx.domain.movies.entity.Like;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final MovieRepository movieRepository;

  // 좋아요 추가
  @Transactional
  public LikeDto create(UUID userId, UUID movieId) {
    User user = userRepository.findById(userId).orElse(null);
    Movie movie = movieRepository.findById(movieId).orElse(null);

    Like like = new Like(user, movie);
    Like created = likeRepository.save(like);

    return LikeDto.from(created);
  }

  // 좋아요 삭제
  @Transactional
  public LikeDto delete(UUID userId, UUID movieId) {
    Like like = likeRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
    likeRepository.delete(like);

    return LikeDto.notLiked(userId, movieId);
  }
}