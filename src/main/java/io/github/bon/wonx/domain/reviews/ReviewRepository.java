package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // 특정 영화의 모든 댓글 조회 (JPQL로 변경)
    @Query("""
        SELECT r FROM Review r
        WHERE r.movie.id = :id
    """)
    List<Review> findByMovieId(@Param("id") UUID id);

    // 특정 영화 ID 목록에 해당하는 리뷰들 (영화, 유저 정보 포함 fetch)
    @Query("""
        SELECT r FROM Review r 
        JOIN FETCH r.movie 
        JOIN FETCH r.user 
        WHERE r.movie.id IN :movieIds
    """)
    List<Review> findByMovieIds(@Param("movieIds") List<UUID> movieIds);

    // 최근 24시간 내 리뷰 수 기준 인기 영화 ID (JPQL로 변경)
    @Query("""
        SELECT r.movie.id
        FROM Review r
        WHERE r.createdAt >= CURRENT_TIMESTAMP - 1 DAY
        GROUP BY r.movie.id
        ORDER BY COUNT(r.id) DESC
    """)
    List<UUID> findPopularVideoIdsInLast24Hours(Pageable pageable);

    // 전체 리뷰 수 기준 인기 영화 ID (JPQL로 변경)
    @Query("""
        SELECT r.movie.id
        FROM Review r
        GROUP BY r.movie.id
        ORDER BY COUNT(r.id) DESC
    """)
    List<UUID> findMostReviewedVideoIds(Pageable pageable);

    // 유저가 작성한 리뷰의 ID 목록 조회
    @Query("SELECT r.id FROM Review r WHERE r.user.id = :userId")
    List<UUID> findReviewedMovieIdsByUser(@Param("userId") UUID userId);
}
