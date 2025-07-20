package io.github.bon.wonx.domain.profile.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.bon.wonx.domain.follow.repository.FollowRepository;
import io.github.bon.wonx.domain.history.WatchHistory;
import io.github.bon.wonx.domain.history.WatchHistoryDto;
import io.github.bon.wonx.domain.history.WatchHistoryRepository;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
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

    private List<MovieDto> mapWithUserLikesAndBookmarks(List<Movie> movies, UUID userId) {
    List<UUID> ids = movies.stream().map(Movie::getId).toList();

    List<UUID> bookmarkedIds = bookmarkRepo.findBookmarkedMovieIdsByUserAndMovieIds(userId, ids);
    List<UUID> likedIds = likeRepo.findLikedMovieIdsByUserAndMovieIds(userId, ids);

    return movies.stream()
            .map(m -> {
                MovieDto dto = MovieDto.from(m);
                dto.setIsBookmarked(bookmarkedIds.contains(m.getId()));
                dto.setIsLiked(likedIds.contains(m.getId()));
                return dto;
            })
            .toList();
    }

    public List<WatchHistoryDto> getMypageWatchHistory(User user) {
        List<WatchHistory> histories = historyRepo.findRecentHistoriesByUser(user.getId());
        List<UUID> movieIds = histories.stream()
                .map(h -> h.getMovie().getId())
                .toList();

        List<UUID> bookmarkedIds = bookmarkRepo.findBookmarkedMovieIdsByUserAndMovieIds(user.getId(), movieIds);
        List<UUID> likedIds = likeRepo.findLikedMovieIdsByUserAndMovieIds(user.getId(), movieIds);

        return histories.stream()
                .map(h -> WatchHistoryDto.from(
                    h,
                    bookmarkedIds.contains(h.getMovie().getId()),
                    likedIds.contains(h.getMovie().getId())
                ))
                .toList();
    }

    public List<MovieDto> getBookmarkedMovies(UUID userId) {
        List<UUID> movieIds = bookmarkRepo.findBookmarkedMovieIdsByUser(userId);
        List<Movie> movies = movieRepo.findAllById(movieIds);
        return mapWithUserLikesAndBookmarks(movies, userId);
    }

    public List<MovieDto> getLikedMovies(UUID userId) {
        List<UUID> ids = likeRepo.findLikedMovieIdsByUser(userId);
        List<Movie> movies = movieRepo.findAllById(ids);
        return mapWithUserLikesAndBookmarks(movies, userId);
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
