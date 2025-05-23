package io.github.bon.wonx.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 서버에서 자주 씀)
        .authorizeHttpRequests(auth -> auth

            .anyRequest().authenticated() // 나머지는 인증 필요
        )
        .formLogin(withDefaults()) // 기본 로그인 폼 사용
        .logout(withDefaults()); // 기본 로그아웃 처리

    return http.build();
  }
}
