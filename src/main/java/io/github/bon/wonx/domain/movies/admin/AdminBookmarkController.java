package io.github.bon.wonx.domain.movies.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.movies.entity.Bookmark;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/bookmarks")
public class AdminBookmarkController {

    private final BookmarkRepository bookmarkRepository;

    @GetMapping
    public ResponseEntity<List<AdminBookmarkDto>> getAllBookmarks() {
        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        return ResponseEntity.ok(bookmarks.stream().map(AdminBookmarkDto::fromEntity).toList());
    }
}
