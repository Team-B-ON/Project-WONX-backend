package io.github.bon.wonx.domain.movies.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
    List<Movie> findByGenresIn(List<Genre> genres);
}