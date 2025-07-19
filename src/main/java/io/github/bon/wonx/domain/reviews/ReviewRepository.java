package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    // 특정 영화의 모든 댓글 조회
    @Query(value = "SELECT * FROM reviews WHERE video_id = :id", nativeQuery = true)
    List<Review> findByMovieId(@Param("id") UUID id);

    @Query("""
    SELECT r FROM Review r 
    JOIN FETCH r.movie 
    JOIN FETCH r.user 
    WHERE r.movie.id IN :movieIds
    """)
    List<Review> findByMovieIds(@Param("movieIds") List<UUID> movieIds);

    // 최근 N시간 내 리뷰 많은 영화 ID (예: 최근 24시간)
    @Query(value = """
        SELECT video_id 
        FROM reviews 
        WHERE created_at >= NOW() - INTERVAL 24 HOUR
        GROUP BY video_id 
        ORDER BY COUNT(*) DESC 
        LIMIT :limit
    """, nativeQuery = true)
    List<UUID> findPopularVideoIdsInLast24Hours(@Param("limit") int limit);

    // 전체 리뷰 수 기준으로 인기 있는 영화 ID
    @Query(value = """
        SELECT video_id 
        FROM reviews 
        GROUP BY video_id 
        ORDER BY COUNT(*) DESC 
        LIMIT :limit
    """, nativeQuery = true)
    List<UUID> findMostReviewedVideoIds(@Param("limit") int limit);

    // 유저가 작성한 리뷰의 ID 목록 조회
    @Query("SELECT r.id FROM Review r WHERE r.user.id = :userId")
    List<UUID> findReviewedMovieIdsByUser(@Param("userId") UUID userId);
}