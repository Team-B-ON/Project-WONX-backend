package io.github.bon.wonx.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated() // 모든 요청은 인증 필요
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // JWT 방식 인증
        .formLogin(login -> login.disable()) // 폼 로그인 사용 안 함
        .logout(logout -> logout.disable()); // 로그아웃도 비활성화

    return http.build();
  }
}
