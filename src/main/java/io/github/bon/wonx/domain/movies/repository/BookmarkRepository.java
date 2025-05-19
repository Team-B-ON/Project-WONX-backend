package io.github.bon.wonx.domain.movies.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.movies.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
    Optional<Bookmark> findByUserIdAndMovieId(UUID userId, UUID movieId);
    void deleteByUserIdAndMovieId(UUID userId, UUID movieId);
}
