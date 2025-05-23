package io.github.bon.wonx.domain.user;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        PlanType userPlan = (PlanType) request.getAttribute("userPlan");
        return ResponseEntity.ok("유저 ID: " + userId + ", 요금제: " + userPlan);
    }
}
