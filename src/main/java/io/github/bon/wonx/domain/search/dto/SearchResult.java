package io.github.bon.wonx.domain.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchResult {

    private List<MovieSearchDto> movies;
    private List<UserSearchDto> users;
    private List<ReviewSearchDto> reviews;
    private List<SuggestionDto> suggestions;

    public static SearchResult of(
        List<MovieSearchDto> movies,
        List<UserSearchDto> users,
        List<ReviewSearchDto> reviews,
        List<SuggestionDto> suggestions
    ) {
        return SearchResult.builder()
            .movies(movies)
            .users(users)
            .reviews(reviews)
            .suggestions(suggestions)
            .build();
    }

    public static SearchResult of(
        List<MovieSearchDto> movies,
        List<UserSearchDto> users,
        List<ReviewSearchDto> reviews
    ) {
        return SearchResult.builder()
            .movies(movies)
            .users(users)
            .reviews(reviews)
            .suggestions(null)
            .build();
    }
}
