package io.github.bon.wonx.domain.auth;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.auth.mail.MailRequestDto;
import io.github.bon.wonx.domain.auth.token.RefreshRequestDto;
import io.github.bon.wonx.domain.auth.token.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1. 이메일을 받아 magic link 전송
    @PostMapping("/send-link")
    public ResponseEntity<Void> sendMagicLink(@RequestBody @Valid MailRequestDto requestDto) {
        authService.sendMagicLink(requestDto);
        return ResponseEntity.ok().build();
    }

    // 2. 사용자가 링크 클릭 → 토큰 인증 → access/refresh 발급
    @GetMapping("/verify")
    public ResponseEntity<TokenResponseDto> verify(@RequestParam String token) {
        // 토큰에서 userId 추출
        UUID userId = UUID.fromString(authService.extractUserIdFromToken(token));
        TokenResponseDto tokens = authService.authenticate(userId);
        return ResponseEntity.ok(tokens);
    }

    // 3. refresh 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshRequestDto requestDto) {
        TokenResponseDto newTokens = authService.refresh(requestDto.getRefreshToken());
        return ResponseEntity.ok(newTokens);
    }
}
