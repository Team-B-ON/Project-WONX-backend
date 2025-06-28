package io.github.bon.wonx.domain.home;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotTalkRepository extends JpaRepository<HotTalk, UUID> {

  long count(); // JPA 기본 제공 메서드, 누적 리뷰 수

  List<HotTalk> findTop3ByOrderByViewCountDescCreatedAtDesc();
}
