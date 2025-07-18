package io.github.bon.wonx.domain.genres;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

    // genre name이 리스트에 포함된 장르들을 찾기
    List<Genre> findByNameIn(List<String> names);
}
