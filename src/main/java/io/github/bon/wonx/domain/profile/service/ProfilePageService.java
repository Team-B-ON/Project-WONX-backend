package io.github.bon.wonx.domain.profile.service;

import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import io.github.bon.wonx.domain.history.repository.WatchHistoryRepository;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.profile.dto.*;
import io.github.bon.wonx.domain.reviews.ReviewRepository;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<MovieDto> getRecentVideos(UUID userId) {
        List<UUID> ids = historyRepo.findRecentVideoIdsByUser(userId);
        return movieRepo.findAllById(ids).stream()
                .map(MovieDto::from)
                .collect(Collectors.toList());
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

    public List<MovieDto> getInProgressVideos(UUID userId) {
        List<UUID> ids = historyRepo.findInProgressVideoIdsByUser(userId);
        return movieRepo.findAllById(ids).stream()
                .map(MovieDto::from)
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
