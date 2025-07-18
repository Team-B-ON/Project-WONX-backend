    package io.github.bon.wonx.domain.profile.dto;

    import java.time.LocalDateTime;
    import java.util.UUID;

    import lombok.Builder;
    import lombok.Getter;

    @Getter @Builder
    public class PublicProfileDto {
        private UUID userId;
        private String email;
        private String nickname;
        private String profileImageUrl;
        private String bio;
        private LocalDateTime joinedAt;

        private boolean isMe;
        private boolean isFollowing;

        private long followerCount;
        private long followingCount;
    }