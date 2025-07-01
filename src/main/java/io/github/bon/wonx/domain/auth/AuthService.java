package io.github.bon.wonx.domain.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.bon.wonx.domain.auth.mail.MailRequestDto;
import io.github.bon.wonx.domain.auth.mail.MailService;
import io.github.bon.wonx.domain.auth.token.JwtProvider;
import io.github.bon.wonx.domain.auth.token.RefreshToken;
import io.github.bon.wonx.domain.auth.token.RefreshTokenRepository;
import io.github.bon.wonx.domain.auth.token.TokenResponseDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Environment env;

    @Transactional
    public void sendMagicLink(MailRequestDto requestDto) {
        String email = requestDto.getEmail();

        // 유저 생성 or 조회
        User user = userRepository.findByEmail(email)
            .orElseGet(() -> {
                String baseNickname = email.split("@")[0];
                String suffix = UUID.randomUUID().toString().substring(0, 6);
                String nickname = baseNickname + "_" + suffix;

                return userRepository.save(User.builder()
                    .email(email)
                    .nickname(nickname)
                    .build());
            });

        // 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // 프론트 URL 구성
        String frontendBase = env.getProperty("app.frontend.url");
        String callbackPath = "/auth/callback";

        String magicLink = UriComponentsBuilder
            .fromHttpUrl(frontendBase + callbackPath)
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .toUriString();

        // 이메일 전송
        mailService.sendMagicLink(email, magicLink);
    }

    @Transactional
    public TokenResponseDto authenticate(UUID userId) {
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<RefreshToken> existing = refreshTokenRepository.findByUser(user);
        if (existing.isPresent()) {
            existing.get().updateToken(refreshToken, LocalDateTime.now().plusDays(7));
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .user(user)
                    .token(refreshToken)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build());
        }

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public String extractUserIdFromToken(String token) {
        return jwtProvider.parseUserId(token);
    }

    @Transactional
    public TokenResponseDto refresh(String refreshToken) {
        String userIdStr = jwtProvider.parseUserId(refreshToken);
        User user = userRepository.findById(UUID.fromString(userIdStr))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RefreshToken stored = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (!stored.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match");
        }

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        String newAccessToken = jwtProvider.createAccessToken(user.getId());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        stored.updateToken(newRefreshToken, LocalDateTime.now().plusDays(7));

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}
