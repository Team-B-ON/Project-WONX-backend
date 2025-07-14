package io.github.bon.wonx.domain.people.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.people.Person;
import io.github.bon.wonx.domain.people.PersonRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/people")
public class AdminPersonController {

    private final PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<List<AdminPersonDto>> getAllPeople() {
        List<Person> people = personRepository.findAll();
        List<AdminPersonDto> result = people.stream()
                .map(AdminPersonDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
