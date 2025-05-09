package io.github.bon.wonx.domain.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
  List<Video> findTop1ByOrderByRatingDesc(); // 평점 높은 콘텐츠를 1개 가져오기 위함 -> service에서 이걸 호출
}
