package io.github.bon.wonx.domain.auth.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);  // 로그아웃 시 토큰 삭제용
}
