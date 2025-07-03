package io.github.bon.wonx.domain.movies.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.BookmarkDto;
import io.github.bon.wonx.domain.movies.entity.Bookmark;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.user.User;
import io.github.bon.wonx.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    // 북마크 추가
    @Transactional
    public BookmarkDto create(UUID userId, UUID movieId) {
        System.out.println("BookmarkService 진입");
        System.out.println("userId: " + userId);
        System.out.println("movieId: " + movieId);

        try {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: " + movieId));
        
        Bookmark bookmark = new Bookmark(user, movie);
        Bookmark created = bookmarkRepository.save(bookmark);
        
        System.out.println("북마크 생성 완료: " + created.getId());
        return BookmarkDto.from(created);

        } catch (Exception e) {
        System.out.println("예외 발생: " + e.getMessage());
        e.printStackTrace();
        throw e;
        }
    }
    
    // 북마크 삭제
    @Transactional
    public BookmarkDto delete(UUID userId, UUID movieId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        }

        return BookmarkDto.notBookmarked(userId, movieId);        
    }
}
