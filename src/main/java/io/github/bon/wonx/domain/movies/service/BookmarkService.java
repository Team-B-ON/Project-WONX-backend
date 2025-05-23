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
    User user = userRepository.findById(userId).orElse(null);
    Movie movie = movieRepository.findById(movieId).orElse(null);

    Bookmark bookmark = new Bookmark(user, movie);
    Bookmark created = bookmarkRepository.save(bookmark);

    return BookmarkDto.from(created);
  }

  // 북마크 삭제
  @Transactional
  public BookmarkDto delete(UUID userId, UUID movieId) {
    Bookmark bookmark = bookmarkRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
    bookmarkRepository.delete(bookmark);

    return BookmarkDto.notBookmarked(userId, movieId);
  }
}
