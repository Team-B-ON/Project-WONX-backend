package io.github.bon.wonx.domain.video.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.bon.wonx.domain.video.entity.Video;
import org.springframework.data.domain.Pageable;

public interface VideoRepository extends JpaRepository<Video, Long> {

  boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

  // 박스오피스 1위 영상 조회
  Optional<Video> findTop1ByOrderByBoxOfficeRankAsc();

  // 개봉 예정작 조회
  List<Video> findByReleaseDateBetweenOrderByReleaseDateAsc(LocalDate start, LocalDate end);

  // 조회수 내림차순 정렬 + 페이징
  List<Video> findAllByOrderByViewCountDesc(Pageable pageable);
}
