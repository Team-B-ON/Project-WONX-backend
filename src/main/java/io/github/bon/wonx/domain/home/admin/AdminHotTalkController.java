package io.github.bon.wonx.domain.home.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.home.HotTalk;
import io.github.bon.wonx.domain.home.HotTalkRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/hot-talks")
public class AdminHotTalkController {

    private final HotTalkRepository hotTalkRepository;

    @GetMapping
    public ResponseEntity<List<AdminHotTalkDto>> getAllHotTalks() {
        List<HotTalk> hotTalks = hotTalkRepository.findAll();
        List<AdminHotTalkDto> result = hotTalks.stream()
                .map(AdminHotTalkDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
