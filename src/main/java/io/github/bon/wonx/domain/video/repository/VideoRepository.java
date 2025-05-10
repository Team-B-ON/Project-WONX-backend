package io.github.bon.wonx.domain.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import io.github.bon.wonx.domain.video.entity.Video;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
  List<Video> findTop1ByOrderByRatingDesc();

  List<Video> findByReleaseDateBetweenOrderByReleaseDateAsc(LocalDate start, LocalDate end);

  List<Video> findAllByOrderByViewCountDesc(Pageable pageable);

  // 박스오피스용 쿼리
  List<Video> findTop10ByOrderByRatingDesc();
}
