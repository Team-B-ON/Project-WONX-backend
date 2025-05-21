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
            .requestMatchers(
                "/api/home/**", // 메인 페이지 (배너, 리뷰 등)
                "/api/search/**", // 검색 기능
                "/api/movies/**", // 영화 관련
                "/api/reviews/**", // 리뷰 조회
                "/api/admin/**" // 나중에 쓸 공개 prefix
            ).permitAll() // ↑ 이 경로들만 로그인 없이 허용
            .anyRequest().authenticated() // 나머지는 인증 필요
        )
        .formLogin(withDefaults()) // 기본 로그인 폼 사용
        .logout(withDefaults()); // 기본 로그아웃 처리

    return http.build();
  }
}
