package io.github.bon.wonx.domain.people;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.movies.entity.MoviePerson;
import io.github.bon.wonx.domain.people.dto.PersonMovieResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    // 특정 인물의 영화 목록 조회
    public PersonMovieResponse moviesOf(UUID id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "영화 조회 실패: " + "대상 인물이 없습니다."));

        List<MoviePerson> relations = person.getMoviePersons();

        List<String> roles = relations.stream()
                    .map(MoviePerson::getRole)
                    .distinct()
                    .toList();

        List<MovieSummaryDto> movies = relations.stream()  
                .map(MovieSummaryDto::from)
                .toList();

        return new PersonMovieResponse(
            person.getId(),
            person.getName(),
            roles,
            movies
        );
    }
}
