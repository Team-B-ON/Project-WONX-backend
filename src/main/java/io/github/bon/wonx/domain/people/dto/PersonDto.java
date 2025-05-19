package io.github.bon.wonx.domain.people.dto;

import java.util.UUID;

import io.github.bon.wonx.domain.people.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private UUID id;
    private String name;
    
    public static PersonDto createPersonDto(Person person) {
        return new PersonDto(
            person.getId(),
            person.getName()
        );
    }
}
