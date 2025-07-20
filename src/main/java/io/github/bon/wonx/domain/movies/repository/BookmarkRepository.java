package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.movies.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    @Query("""
        SELECT b FROM Bookmark b
        WHERE b.user.id = :userId AND b.movie.id = :movieId
    """)
    Optional<Bookmark> findByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") UUID movieId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Bookmark b
        WHERE b.user.id = :userId AND b.movie.id = :movieId
    """)
    void deleteByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") UUID movieId);

    @Query("""
        SELECT b.movie.id FROM Bookmark b
        WHERE b.user.id = :userId
    """)
    List<UUID> findBookmarkedMovieIdsByUser(@Param("userId") UUID userId);

    @Query("""
        SELECT b.movie.id FROM Bookmark b
        WHERE b.user.id = :userId AND b.movie.id IN :movieIds
    """)
    List<UUID> findBookmarkedMovieIdsByUserAndMovieIds(@Param("userId") UUID userId, @Param("movieIds") List<UUID> movieIds);
}
