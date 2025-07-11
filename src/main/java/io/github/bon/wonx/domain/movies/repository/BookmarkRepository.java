package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.movies.entity.Bookmark;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
    Optional<Bookmark> findByUserIdAndMovieId(UUID userId, UUID movieId);
    void deleteByUserIdAndMovieId(UUID userId, UUID movieId);

    @Query("SELECT b.movie.id FROM Bookmark b WHERE b.user.id = :userId")
    List<UUID> findBookmarkedMovieIdsByUser(@Param("userId") UUID userId);
}
