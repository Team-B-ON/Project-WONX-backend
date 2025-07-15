package io.github.bon.wonx.domain.history.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.history.WatchHistory;
import io.github.bon.wonx.domain.history.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/watch-histories")
public class AdminWatchHistoryController {

    private final WatchHistoryRepository watchHistoryRepository;

    @GetMapping
    public ResponseEntity<List<AdminWatchHistoryDto>> getAllHistories() {
        List<WatchHistory> histories = watchHistoryRepository.findAll();
        List<AdminWatchHistoryDto> result = histories.stream()
                .map(AdminWatchHistoryDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
