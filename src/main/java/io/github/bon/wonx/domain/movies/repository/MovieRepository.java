package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.genres.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

    // 장르 객체 기반으로 조회
    List<Movie> findByGenresIn(List<Genre> genres);
}
