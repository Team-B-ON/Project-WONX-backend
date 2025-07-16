package io.github.bon.wonx.domain.history;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, UUID> {

    // 전체 시청 기록
    @Query("SELECT h FROM WatchHistory h WHERE h.user.id = :userId ORDER BY h.watchedAt DESC")
    List<WatchHistory> findRecentHistoriesByUser(UUID userId);

    // 이어보기 중인 시청 기록
    @Query("SELECT h FROM WatchHistory h WHERE h.user.id = :userId AND h.isCompleted = false ORDER BY h.updatedAt DESC")
    List<WatchHistory> findInProgressHistoriesByUser(UUID userId);

    // movieId만 추출 (프로필 페이지에서 사용)
    @Query("SELECT h.movie.id FROM WatchHistory h WHERE h.user.id = :userId ORDER BY h.watchedAt DESC")
    List<UUID> findRecentVideoIdsByUser(UUID userId);

    @Query("SELECT h.movie.id FROM WatchHistory h WHERE h.user.id = :userId AND h.isCompleted = false ORDER BY h.updatedAt DESC")
    List<UUID> findInProgressVideoIdsByUser(UUID userId);

    // 많이 본 영화 (movieId와 count만 반환)
    @Query("SELECT h.movie.id, COUNT(h) as cnt FROM WatchHistory h GROUP BY h.movie.id ORDER BY cnt DESC")
    List<Object[]> findTopWatchedMovies();  // [0]: movieId, [1]: count

    @Query("""
    SELECT g.name, COUNT(g.name) as cnt
    FROM WatchHistory h
    JOIN h.movie m
    JOIN m.genres g
    WHERE h.user.id = :userId
    GROUP BY g.name
    ORDER BY cnt DESC
    """)
    List<Object[]> findTopGenresByUserId(UUID userId);
}
