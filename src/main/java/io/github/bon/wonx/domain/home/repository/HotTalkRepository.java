package io.github.bon.wonx.domain.home.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.bon.wonx.domain.home.entity.HotTalk;

public interface HotTalkRepository extends JpaRepository<HotTalk, UUID> {
  List<HotTalk> findTop3ByOrderByViewCountDescCreatedAtDesc();
}
