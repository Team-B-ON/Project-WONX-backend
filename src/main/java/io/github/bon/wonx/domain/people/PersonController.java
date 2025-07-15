package io.github.bon.wonx.domain.people;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import io.github.bon.wonx.domain.people.dto.PersonMovieResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    // 특정 인물의 영화 목록 조회
    @GetMapping("/api/people/{id}/movies")
    public ResponseEntity<PersonMovieResponse> getMoviesByPerson(
            @PathVariable UUID id,
            @RequestAttribute(value = "userId", required = false) UUID userId
    ) {
        return ResponseEntity.ok(personService.moviesOf(id, userId));
    }
}
