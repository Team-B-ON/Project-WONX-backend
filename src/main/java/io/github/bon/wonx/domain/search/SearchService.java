package io.github.bon.wonx.domain.search;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.reviews.Review;
import io.github.bon.wonx.domain.reviews.dto.ReviewDto;
import io.github.bon.wonx.domain.search.util.HangulUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<MovieDto> searchMoviesByTitle(String keyword, String sort) {
        List<Movie> result;
        if (HangulUtils.isChoseongOnly(keyword)) {
            String regex = HangulUtils.choseongToRegex(keyword);
            result = searchRepository.searchByTitleRegex(regex);
        } else {
            result = searchRepository.searchMoviesByTitle(keyword, sort);
        }
        return result.stream().map(MovieDto::from).toList();
    }

    public List<MovieDto> searchMoviesByGenre(String genreName, String sort) {
        List<Movie> result;
        if (HangulUtils.isChoseongOnly(genreName)) {
            String regex = HangulUtils.choseongToRegex(genreName);
            result = searchRepository.searchByGenreRegex(regex); // 정렬 없는 native
        } else {
            result = searchRepository.searchMoviesByGenre(genreName, sort);
        }
        return result.stream().map(MovieDto::from).toList();
    }

    public List<MovieDto> searchMoviesByPerson(String keyword, String sort) {
        List<Movie> result;
        if (HangulUtils.isChoseongOnly(keyword)) {
            String regex = HangulUtils.choseongToRegex(keyword);
            result = searchRepository.searchByPersonRegex(regex);
        } else {
            result = searchRepository.searchMoviesByPerson(keyword, sort);
        }
        return result.stream().map(MovieDto::from).toList();
    }

    public List<ReviewDto> searchReviews(String keyword, UUID currentUserId) {
        List<Review> result;
        if (HangulUtils.isChoseongOnly(keyword)) {
            String regex = HangulUtils.choseongToRegex(keyword);
            result = searchRepository.searchReviewByRegex(regex);
        } else {
            result = searchRepository.searchReviewsByContent(keyword);
        }
        return result.stream().map(r -> ReviewDto.from(r, currentUserId)).toList();
    }

    public List<String> autocompleteMovies(String keyword) {
        return searchRepository.autocompleteMovieTitles(keyword);
    }
    public List<String> autocompletePeople(String keyword) {
        return searchRepository.autocompletePersonNames(keyword);
    }

    public List<String> autocompleteGenres(String keyword) {
        return searchRepository.autocompleteGenreNames(keyword);
    }

    public List<String> autocompleteReviews(String keyword) {
        return searchRepository.autocompleteReviewPhrases(keyword);
    }
}
