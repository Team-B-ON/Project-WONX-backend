package io.github.bon.wonx.domain.movies.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bon.wonx.domain.movies.dto.MovieDto;
import io.github.bon.wonx.domain.movies.entity.Genre;
import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 영화 상세 정보 조회
    public MovieDto details(UUID id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        return MovieDto.from(movie);
    }

    // 함께 시청된 콘텐츠 조회
    public List<MovieDto> relatedContents(UUID id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null || movie.getGenres().isEmpty()) return List.of();

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
}

