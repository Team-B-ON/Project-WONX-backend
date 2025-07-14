package io.github.bon.wonx.domain.people.admin;

import java.util.UUID;

import io.github.bon.wonx.domain.people.Person;

public record AdminPersonDto(
    UUID id,
    String name
) {
    public static AdminPersonDto fromEntity(Person person) {
        return new AdminPersonDto(
            person.getId(),
            person.getName()
        );
    }
}
