package io.github.bon.wonx.domain.user.admin;

import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserDto> result = users.stream()
                .map(AdminUserDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
