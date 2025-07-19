package io.github.bon.wonx.domain.profile.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import io.github.bon.wonx.domain.history.WatchHistoryRepository;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.profile.dto.ProfileUpdateRequest;
import io.github.bon.wonx.domain.profile.dto.PublicProfileDto;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfilePageService {

    private final UserRepository    userRepo;
    private final FollowRepository  followRepo;
    private final WatchHistoryRepository historyRepo;
    private final MovieRepository movieRepo;
    private final BookmarkRepository bookmarkRepo;
    private final LikeRepository likeRepo;
    private final ReviewRepository reviewRepo;

    @Transactional
    public PublicProfileDto getProfileDetail(UUID requesterId, UUID targetId) {
        User user = userRepo.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return buildDto(user, requesterId);
    }

    private PublicProfileDto buildDto(User user, UUID requesterId) {
        UUID profileId = user.getId();
        boolean isMe = profileId.equals(requesterId);
        boolean isFollowing = !isMe && followRepo.existsById_FollowerIdAndId_FolloweeId(requesterId, profileId);
        long followerCount = followRepo.countById_FolloweeId(profileId);
        long followingCount = followRepo.countById_FollowerId(profileId);

        return PublicProfileDto.builder()
                .userId(profileId)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .joinedAt(user.getCreatedAt())
                .isMe(isMe)
                .isFollowing(isFollowing)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    public List<MovieDto> getBookmarkedMovies(UUID userId) {
        List<UUID> movieIds = bookmarkRepo.findBookmarkedMovieIdsByUser(userId);
        return movieRepo.findAllById(movieIds).stream()
                .map(MovieDto::from)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getLikedMovies(UUID userId) {
        List<UUID> ids = likeRepo.findLikedMovieIdsByUser(userId);
        return movieRepo.findAllById(ids).stream()
                .map(MovieDto::from)
                .toList();
    }

    public List<ReviewDto> getMyReviews(UUID userId) {
        List<UUID> ids = reviewRepo.findReviewedMovieIdsByUser(userId);
        return reviewRepo.findAllById(ids).stream()
                .map(ReviewDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicProfileDto updateProfile(UUID userId, ProfileUpdateRequest dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.getNickname() != null) {
            user.updateNickname(dto.getNickname());
        }
        if (dto.getBio() != null) {
            user.updateBio(dto.getBio());
        }
        if (dto.getProfileImageUrl() != null) {
            user.updateProfileImageUrl(dto.getProfileImageUrl());
        }

        // 변경된 엔티티를 DTO로 변환하여 반환
        return buildDto(user, userId);
    }
}
