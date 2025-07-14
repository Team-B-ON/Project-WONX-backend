package io.github.bon.wonx.domain.user.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.user.User;

public record AdminUserDto(
    UUID id,
    String email,
    String nickname,
    String planType,
    LocalDateTime createdAt
) {
    public static AdminUserDto fromEntity(User user) {
        return new AdminUserDto(
            user.getId(),
            user.getEmail(),
            user.getNickname(),
            user.getPlanType().name(),
            user.getCreatedAt()
        );
    }
}
