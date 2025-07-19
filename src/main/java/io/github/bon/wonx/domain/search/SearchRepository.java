package io.github.bon.wonx.domain.search;

import java.util.List;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.reviews.Review;

public interface SearchRepository {
    List<Movie> searchMoviesByTitle(String keyword, String sort);
    List<Movie> searchMoviesByGenre(String genreName, String sort);
    List<Movie> searchMoviesByPerson(String personName, String sort);
    List<Review> searchReviewsByContent(String keyword);

    // 초성 대응용 NativeQuery (정규식)
    List<Movie> searchByTitleRegex(String regex);
    List<Movie> searchByGenreRegex(String regex);
    List<Movie> searchByPersonRegex(String regex);
    List<Review> searchReviewByRegex(String regex);

    List<String> autocompletePersonNames(String keyword);
    List<String> autocompleteMovieTitles(String keyword);
    List<String> autocompleteGenreNames(String keyword);
    List<String> autocompleteReviewPhrases(String keyword);
}
