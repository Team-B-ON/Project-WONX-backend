package io.github.bon.wonx.domain.auth.token;

import lombok.Getter;

@Getter
public class RefreshRequestDto {
    private String refreshToken;
}
