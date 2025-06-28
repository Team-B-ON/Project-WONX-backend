package io.github.bon.wonx.domain.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    // 특정 영화의 모든 댓글 조회
    @Query(value = "SELECT * FROM reviews WHERE video_id = :id", nativeQuery = true)
    List<Review> findByMovieId(UUID id);

    // 여러 영화의 리뷰 조회 (← 이거 추가!)
    List<Review> findByMovieIdIn(List<UUID> movieIds);

}