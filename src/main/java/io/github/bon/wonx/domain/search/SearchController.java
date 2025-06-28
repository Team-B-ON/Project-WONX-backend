package io.github.bon.wonx.domain.search;

import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

  private final SearchService searchService;

  @GetMapping
  public ResponseEntity<SearchResult> search(@RequestParam String keyword) {
    return ResponseEntity.ok(searchService.searchAll(keyword));
  }
}