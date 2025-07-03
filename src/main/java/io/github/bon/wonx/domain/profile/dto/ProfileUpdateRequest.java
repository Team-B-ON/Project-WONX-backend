package io.github.bon.wonx.domain.profile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ProfileUpdateRequest {
    private String nickname;
    private String bio;
    private String profileImageUrl;
}