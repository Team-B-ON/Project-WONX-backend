package io.github.bon.wonx.domain.movies.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.dto.BookmarkDto;
import io.github.bon.wonx.domain.movies.service.BookmarkService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies/{id}/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
  private final BookmarkService bookmarkService;

  // 북마크 추가
  @PostMapping("")
  public ResponseEntity<BookmarkDto> create(@PathVariable UUID id) {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111"); // 임시 id
    BookmarkDto createdDto = bookmarkService.create(userId, id);
    return ResponseEntity.ok(createdDto);
  }

  // 북마크 취소
  @DeleteMapping("")
  public ResponseEntity<BookmarkDto> delete(@PathVariable UUID id) {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111"); // 임시 id
    BookmarkDto deletedDto = bookmarkService.delete(userId, id);
    return ResponseEntity.ok(deletedDto);
  }
}