package io.github.bon.wonx.domain.profile.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryDto {
    private UUID userId;
    private String nickname;
    private String profileImageUrl;
}
