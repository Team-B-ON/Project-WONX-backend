package io.github.bon.wonx.domain.movies.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.movies.entity.Like;

public interface LikeRepository extends JpaRepository<Like, UUID> {
  Optional<Like> findByUserIdAndMovieId(UUID userId, UUID movieId);

  void deleteByUserIdAndMovieId(UUID userId, UUID movieId);
}