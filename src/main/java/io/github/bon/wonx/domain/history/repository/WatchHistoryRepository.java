package io.github.bon.wonx.domain.history.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.bon.wonx.domain.history.entity.WatchHistory;

public interface WatchHistoryRepository
        extends JpaRepository<WatchHistory, UUID> {

    @Query("SELECT h.movie.id FROM WatchHistory h WHERE h.user.id = :userId ORDER BY h.watchedAt DESC")
    List<UUID> findRecentVideoIdsByUser(UUID userId);

    @Query("SELECT h.movie.id FROM WatchHistory h WHERE h.user.id = :userId AND h.isCompleted = false ORDER BY h.updatedAt DESC")
    List<UUID> findInProgressVideoIdsByUser(UUID userId);
}
