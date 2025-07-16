package io.github.bon.wonx.domain.movies.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.genres.Genre;
import io.github.bon.wonx.domain.genres.dto.GenreDto;
import io.github.bon.wonx.domain.movies.dto.MovieDetailDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.entity.MoviePerson;
import io.github.bon.wonx.domain.movies.repository.BookmarkRepository;
import io.github.bon.wonx.domain.movies.repository.LikeRepository;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import io.github.bon.wonx.domain.people.dto.PersonDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 영화 상세 정보 조회
    public MovieDetailDto details(UUID movieId, UUID userId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        
        // 1. 좋아요/북마크 여부 확인
        boolean isLiked = userId != null && likeRepository.findByUserIdAndMovieId(userId, movieId).isPresent();
        boolean isBookmarked = userId != null && bookmarkRepository.findByUserIdAndMovieId(userId, movieId).isPresent();

        // 2. 인물 정보 분류
        List<PersonDto> actors = new ArrayList<>();
        List<PersonDto> directors = new ArrayList<>();
        List<PersonDto> screenwriters = new ArrayList<>();

        for (MoviePerson mp : movie.getMoviePersons()) {
            PersonDto person = new PersonDto(mp.getPerson().getId(), mp.getPerson().getName());
            switch (mp.getRole()) {
                case "actor" -> actors.add(person);
                case "director" -> directors.add(person);
                case "screenwriter" -> screenwriters.add(person);
            }
        }

        // 3. 장르 변환
        List<GenreDto> genres = movie.getGenres().stream()
            .map(g -> new GenreDto(g.getId(), g.getName()))
            .toList();
        
        // 4. DTO 빌드
        return MovieDetailDto.builder()
            .movieId(movie.getId())
            .title(movie.getTitle())
            .posterUrl(movie.getPosterUrl())
            .mainImg(movie.getMainImg())
            .releaseDate(movie.getReleaseDate().toString())
            .durationMinutes(movie.getDurationMinutes())
            .ageRating(movie.getAgeRating())
            .description(movie.getDescription())
            .actors(actors)
            .directors(directors)
            .screenwriters(screenwriters)
            .genres(genres)
            .isBookmarked(isBookmarked)
            .isLiked(isLiked)
            .build();
    }

    // 함께 시청된 콘텐츠 조회
    public List<MovieDto> relatedContents(UUID id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null || movie.getGenres().isEmpty())
            return List.of();

        List<Genre> genres = movie.getGenres();
        List<Movie> related = movieRepository.findByGenresIn(genres);

        return related.stream()
                .filter(m -> !m.getId().equals(id))
                .map(MovieDto::from)
                .toList();
    }

    // Movie 엔티티 직접 조회
    public Movie findEntity(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }

    // Presigned URL 생성
    public String generatePresignedUrl(Movie movie) {
        String objectKey = "videos/" + movie.getId() + ".mp4";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public List<MovieDto> findByIds(List<UUID> ids) {
        return movieRepository.findAllById(ids).stream()
                .map(MovieDto::from)
                .toList();
    }
}