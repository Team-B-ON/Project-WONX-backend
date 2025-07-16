// src/main/java/io/github/bon/wonx/domain/home/HomeRepository.java
package io.github.bon.wonx.domain.home;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.bon.wonx.domain.movies.entity.Movie;

public interface HomeRepository extends JpaRepository<Movie, UUID> {

    // 홈 배너용 랜덤 영화
    @Query(value = "SELECT * FROM videos ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Movie> findRandomMovie();

    // 핫 무비
    List<Movie> findTop10ByOrderByViewCountDesc();

    // 추천용 장르 기반 조회
    List<Movie> findTop10ByGenres_NameInOrderByViewCountDesc(List<String> genreNames);
}
