package io.github.bon.wonx.global.auth;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bon.wonx.domain.auth.token.JwtProvider;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. 인증 없이 접근 가능한 경로
        if (
            path.equals("/api/auth/send-link") ||
            path.equals("/api/auth/refresh") ||
            path.startsWith("/api/home") ||
            path.startsWith("/api/movies") ||
            path.startsWith("/api/genres") ||
            path.startsWith("/api/people") ||
            path.startsWith("/api/mypage")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 나머지 경로는 토큰 검증
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String userIdStr = jwtProvider.parseUserId(token);
                UUID userId = UUID.fromString(userIdStr);

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                request.setAttribute("userId", user.getId());
                request.setAttribute("userPlan", user.getPlanType());

                filterChain.doFilter(request, response); // 인증 성공 → 다음 필터로
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired.");
            } catch (MalformedJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Malformed token.");
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token.");
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid user ID format.");
            } catch (NoSuchElementException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found.");
            } catch (Exception e) {
                e.printStackTrace(); // ✅ 로그
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("An unexpected server error occurred.");
            }
        } else {
            // 토큰 아예 없음
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization header");
        }
    }

}
