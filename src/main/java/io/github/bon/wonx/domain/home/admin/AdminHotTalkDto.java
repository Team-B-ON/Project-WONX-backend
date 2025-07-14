package io.github.bon.wonx.domain.home.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.home.HotTalk;

public record AdminHotTalkDto(
    UUID id,
    String content,
    int viewCount,
    LocalDateTime createdAt,
    UUID movieId
) {
    public static AdminHotTalkDto fromEntity(HotTalk hotTalk) {
        return new AdminHotTalkDto(
            hotTalk.getId(),
            hotTalk.getContent(),
            hotTalk.getViewCount(),
            hotTalk.getCreatedAt(),
            hotTalk.getMovie() != null ? hotTalk.getMovie().getId() : null
        );
    }
}
