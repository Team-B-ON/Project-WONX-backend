package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.movies.entity.Like;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    @Query("""
        SELECT l FROM Like l
        WHERE l.user.id = :userId AND l.movie.id = :movieId
    """)
    Optional<Like> findByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") UUID movieId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Like l
        WHERE l.user.id = :userId AND l.movie.id = :movieId
    """)
    void deleteByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") UUID movieId);

    List<Like> findByUserId(UUID userId);

    @Query("""
        SELECT l.movie.id FROM Like l
        WHERE l.user.id = :userId
    """)
    List<UUID> findLikedMovieIdsByUser(@Param("userId") UUID userId);

    @Query("""
        SELECT l.movie.id FROM Like l
        WHERE l.user.id = :userId AND l.movie.id IN :movieIds
    """)
    List<UUID> findLikedMovieIdsByUserAndMovieIds(@Param("userId") UUID userId, @Param("movieIds") List<UUID> movieIds);
}
