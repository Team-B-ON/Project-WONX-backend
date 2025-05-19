package io.github.bon.wonx.domain.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import io.github.bon.wonx.domain.movies.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

}
