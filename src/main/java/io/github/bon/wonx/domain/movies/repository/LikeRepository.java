package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.bon.wonx.domain.movies.entity.Like;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserIdAndMovieId(UUID userId, UUID movieId);

    void deleteByUserIdAndMovieId(UUID userId, UUID movieId);

    // 사용자가 좋아요한 영화 목록 전체 조회 -> 홈 사용자 추천 콘텐츠
    List<Like> findByUserId(UUID userId);

    @Query("SELECT l.movie.id FROM Like l WHERE l.user.id = :userId")
    List<UUID> findLikedMovieIdsByUser(@Param("userId") UUID userId);

    @Query("SELECT l.movie.id FROM Like l WHERE l.user.id = :userId AND l.movie.id IN :movieIds")
    List<UUID> findLikedMovieIdsByUserAndMovieIds(@Param("userId") UUID userId, @Param("movieIds") List<UUID> movieIds);

}