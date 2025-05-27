package io.github.bon.wonx.domain.profile.DTO;

import java.util.UUID;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicProfileDto {
  private UUID userId;
  private String email;
  private String nickname;
  private String profileImageUrl;
  private String bio;
  private LocalDateTime joinedAt;

  private long followingCount;
  private long followerCount;
  private boolean isFollowing;

  // private List<> topGenres;
  private String favoriteGenre;
  private String favoriteActorName;
  private String favoriteDirectorName;
  // private List<> keywords;

}
