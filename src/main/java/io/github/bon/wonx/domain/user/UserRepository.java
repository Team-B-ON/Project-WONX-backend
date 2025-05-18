package io.github.bon.wonx.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 찾기
    Optional<User> findByNickname(String nickname);

    // 닉네임이 특정 문자열을 포함하는 사용자 찾기 (검색용)
    Optional<User> findByNicknameContainingIgnoreCase(String nickname);
}
