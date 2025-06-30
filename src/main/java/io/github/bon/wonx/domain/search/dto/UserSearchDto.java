package io.github.bon.wonx.domain.search.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSearchDto {

  private UUID id;
  private String nickname;
  private String profileImageUrl;

  public static UserSearchDto from(User user) {
    return UserSearchDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }
}