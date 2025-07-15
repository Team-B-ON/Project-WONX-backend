package io.github.bon.wonx.domain.people;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MoviePerson;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.people.dto.PersonMovieResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;

    // 특정 인물의 영화 목록 조회
    @Transactional(readOnly = true)
    public PersonMovieResponse moviesOf(UUID id, UUID userId) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "영화 조회 실패: " + "대상 인물이 없습니다."));

        List<MoviePerson> relations = personRepository.findByPersonIdWithMovie(id);

        List<String> roles = relations.stream()
                    .map(MoviePerson::getRole)
                    .distinct()
                    .toList();

        List<MovieSummaryDto> movies = relations.stream()  
                .map(rel -> {
                    Movie movie = rel.getMovie();
                    boolean isBookmarked = bookmarkRepository.findByUserIdAndMovieId(userId, movie.getId()).isPresent();
                    boolean isLiked = likeRepository.findByUserIdAndMovieId(userId, movie.getId()).isPresent();
                    return MovieSummaryDto.from(movie, isBookmarked, isLiked);
                })
                .toList();

        return new PersonMovieResponse(
            person.getId(),
            person.getName(),
            roles,
            movies
        );
    }
}
