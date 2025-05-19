package io.github.bon.wonx.domain.people;

import java.util.List;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.MoviePerson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "people")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column
    private String name;

    @OneToMany(mappedBy = "person")
    private List<MoviePerson> moviePersons;
}
