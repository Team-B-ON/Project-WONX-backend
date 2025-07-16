package io.github.bon.wonx.domain.genres;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.genres.dto.GenreGroupedMoviesResponse;
import io.github.bon.wonx.domain.genres.dto.SortedGenreMoviesResponse;
import io.github.bon.wonx.domain.genres.dto.GenreGroupedMoviesResponse.SubgenreGroup;
import io.github.bon.wonx.domain.movies.dto.MovieSummaryDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;

    public SortedGenreMoviesResponse getSortedMovies(UUID genreId, String sort, UUID userId, int offset, int limit) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre"));

        List<Movie> all = movieRepository.findByGenresIn(List.of(genre));

        Comparator<Movie> comparator = switch (sort) {
            case "releaseDateAsc" -> Comparator.comparing(Movie::getReleaseDate);
            case "titleAsc" -> Comparator.comparing(Movie::getTitle);
            case "titleDesc" -> Comparator.comparing(Movie::getTitle).reversed();
            case "releaseDateDesc" -> Comparator.comparing(Movie::getReleaseDate).reversed();
            default -> Comparator.comparing(Movie::getReleaseDate).reversed();
        };

        List<Movie> sorted = all.stream().sorted(comparator).toList();
        List<Movie> paged = sorted.stream().skip(offset).limit(limit).toList();

        Set<UUID> bookmarks = new HashSet<>(bookmarkRepository.findBookmarkedMovieIdsByUser(userId));
        Set<UUID> likes = new HashSet<>(likeRepository.findLikedMovieIdsByUser(userId));

        List<MovieSummaryDto> dtos = paged.stream()
                .map(m -> MovieSummaryDto.from(m, bookmarks.contains(m.getId()), likes.contains(m.getId())))
                .toList();

        return new SortedGenreMoviesResponse(
            genre.getId(),
            genre.getName(),
            offset,
            limit,
            offset + limit < sorted.size(),
            sort,
            dtos
        );
    }

    public GenreGroupedMoviesResponse getGroupedBySubgenre(UUID genreId, UUID userId, int offset, int limitPerGroup) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre"));

        List<Movie> movies = movieRepository.findByGenresIn(List.of(genre));

        Set<UUID> bookmarks = new HashSet<>(bookmarkRepository.findBookmarkedMovieIdsByUser(userId));
        Set<UUID> likes = new HashSet<>(likeRepository.findLikedMovieIdsByUser(userId));

        Map<Genre, List<Movie>> grouped = new LinkedHashMap<>();

        for (Movie movie : movies) {
            for (Genre g : movie.getGenres()) {
                if (!g.getId().equals(genreId)) {
                    grouped.computeIfAbsent(g, k -> new ArrayList<>()).add(movie);
                }
            }
        }

        List<SubgenreGroup> groups = grouped.entrySet().stream()
                .skip(offset)
                .limit(limitPerGroup)
                .map(entry -> {
                    Genre subgenre = entry.getKey();
                    List<MovieSummaryDto> movieDtos = entry.getValue().stream()
                            .limit(40)
                            .map(m -> MovieSummaryDto.from(m, bookmarks.contains(m.getId()), likes.contains(m.getId())))
                            .toList();
                    return new SubgenreGroup(subgenre.getName(), subgenre.getId(), movieDtos);
                })
                .toList();

        return new GenreGroupedMoviesResponse(
            genre.getId(),
            genre.getName(),
            offset,
            limitPerGroup,
            offset + limitPerGroup < grouped.size(),
            "subgenre",
            groups
        );
    }

}
