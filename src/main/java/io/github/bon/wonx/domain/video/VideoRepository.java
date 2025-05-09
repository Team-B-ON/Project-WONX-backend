package io.github.bon.wonx.domain.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
  // 배너용 콘텐츠 1개 가져오기
  List<Video> findTop1ByOrderByRatingDesc();

  // 오늘 날짜 기준 1주일 이내에 공개될 콘텐츠만 보여주기
  List<Video> findByReleaseDateBetweenOrderByReleaseDateAsc(LocalDate start, LocalDate end);

}
