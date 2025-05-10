package io.github.bon.wonx.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  // 조회수 기준 내림차순 + 최신순
  List<Review> findAllByOrderByViewCountDescCreatedAtDesc(Pageable pageable);

}
