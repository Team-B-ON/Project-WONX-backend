package io.github.bon.wonx.domain.video.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.bon.wonx.domain.video.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

  boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

  // 박스오피스 1위 영상 조회 메서드
  Optional<Video> findTop1ByOrderByBoxOfficeRankAsc();
}
