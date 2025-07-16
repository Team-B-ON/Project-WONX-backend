package io.github.bon.wonx.domain.search;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.bon.wonx.domain.movies.entity.Movie;

public interface SearchRepository extends JpaRepository<Movie, UUID> {

    // 제목에 키워드 포함된 영화 검색 (대소문자 무시)
    List<Movie> findByTitleContainingIgnoreCase(String keyword);

    // 모든 영화 제목 가져오기 (유사어 추천용)
    @Query("SELECT m.title FROM Movie m")
    List<String> findAllTitles();
}
