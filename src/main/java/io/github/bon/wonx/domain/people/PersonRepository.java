package io.github.bon.wonx.domain.people;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.bon.wonx.domain.movies.entity.MoviePerson;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    @Query("SELECT mp FROM MoviePerson mp JOIN FETCH mp.movie WHERE mp.person.id = :personId")
    List<MoviePerson> findByPersonIdWithMovie(@Param("personId") UUID personId);
}
