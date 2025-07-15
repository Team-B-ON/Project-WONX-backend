package io.github.bon.wonx.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String bio;
    private PlanType planType;
    private LocalDateTime createdAt;
}
