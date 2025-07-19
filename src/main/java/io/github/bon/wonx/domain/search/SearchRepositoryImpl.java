package io.github.bon.wonx.domain.search;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.reviews.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {

    private final EntityManager em;

    @Override
    public List<Movie> searchMoviesByTitle(String keyword, String sort) {
        String orderClause = switch (sort) {
            case "rating" -> " ORDER BY m.rating DESC";
            case "release" -> " ORDER BY m.releaseDate DESC";
            default -> "";
        };

        String jpql = """
            SELECT m FROM Movie m
            WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """ + orderClause;

        return em.createQuery(jpql, Movie.class)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    @Override
    public List<Movie> searchMoviesByGenre(String genreName, String sort) {
        String orderClause = switch (sort) {
            case "rating" -> " ORDER BY m.rating DESC";
            case "release" -> " ORDER BY m.releaseDate DESC";
            default -> "";
        };

        String jpql = """
            SELECT DISTINCT m FROM Movie m
            JOIN m.genres g
            WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :genreName, '%'))
        """ + orderClause;

        return em.createQuery(jpql, Movie.class)
                .setParameter("genreName", genreName)
                .getResultList();
    }

    @Override
    public List<Movie> searchMoviesByPerson(String personName, String sort) {
        String orderClause = switch (sort) {
            case "rating" -> " ORDER BY m.rating DESC";
            case "release" -> " ORDER BY m.releaseDate DESC";
            default -> "";
        };

        String jpql = """
            SELECT DISTINCT m FROM Movie m
            JOIN m.moviePersons mp
            JOIN mp.person p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :personName, '%'))
        """ + orderClause;

        return em.createQuery(jpql, Movie.class)
                .setParameter("personName", personName)
                .getResultList();
    }

    @Override
    public List<Review> searchReviewsByContent(String keyword) {
        return em.createQuery("""
            SELECT r FROM Review r
            WHERE LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """, Review.class)
        .setParameter("keyword", keyword)
        .getResultList();
    }

    @Override
    public List<String> autocompleteMovieTitles(String keyword) {
        return em.createQuery("""
            SELECT DISTINCT m.title FROM Movie m
            WHERE m.title LIKE CONCAT(:keyword, '%')
        """, String.class)
        .setParameter("keyword", keyword)
        .setMaxResults(10)
        .getResultList();
    }

    @Override
    public List<String> autocompletePersonNames(String keyword) {
        return em.createQuery("""
            SELECT DISTINCT p.name FROM Person p
            WHERE p.name LIKE CONCAT(:keyword, '%')
        """, String.class)
        .setParameter("keyword", keyword)
        .setMaxResults(10)
        .getResultList();
    }

    @Override
    public List<String> autocompleteGenreNames(String keyword) {
        return em.createQuery("""
            SELECT DISTINCT g.name FROM Genre g
            WHERE g.name LIKE CONCAT(:keyword, '%')
        """, String.class)
        .setParameter("keyword", keyword)
        .setMaxResults(10)
        .getResultList();
    }

    @Override
    public List<String> autocompleteReviewPhrases(String keyword) {
        return em.createQuery("""
            SELECT DISTINCT r.content FROM Review r
            WHERE r.content LIKE CONCAT(:keyword, '%')
        """, String.class)
        .setParameter("keyword", keyword)
        .setMaxResults(10)
        .getResultList();
    }

    // -------------------
    // 초성 검색용 Native SQL
    // -------------------

    @Override
    public List<Movie> searchByTitleRegex(String regex) {
        Query query = em.createNativeQuery("""
            SELECT * FROM videos
            WHERE title REGEXP :regex
        """, Movie.class);
        query.setParameter("regex", regex);
        return query.getResultList();
    }

    @Override
    public List<Movie> searchByGenreRegex(String regex) {
        Query query = em.createNativeQuery("""
            SELECT DISTINCT m.* FROM videos m
            JOIN video_genre vg ON m.id = vg.video_id
            JOIN genres g ON vg.genre_id = g.id
            WHERE g.name REGEXP :regex
        """, Movie.class);
        query.setParameter("regex", regex);
        return query.getResultList();
    }

    @Override
    public List<Movie> searchByPersonRegex(String regex) {
        Query query = em.createNativeQuery("""
            SELECT DISTINCT m.* FROM videos m
            JOIN movie_person mp ON m.id = mp.movie_id
            JOIN person p ON mp.person_id = p.id
            WHERE p.name REGEXP :regex
        """, Movie.class);
        query.setParameter("regex", regex);
        return query.getResultList();
    }

    @Override
    public List<Review> searchReviewByRegex(String regex) {
        Query query = em.createNativeQuery("""
            SELECT * FROM reviews
            WHERE content REGEXP :regex
        """, Review.class);
        query.setParameter("regex", regex);
        return query.getResultList();
    }
}
