package io.github.bon.wonx.domain.movies.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.github.bon.wonx.domain.genres.Genre;
import io.github.bon.wonx.domain.home.dto.HotMovieDto;
import io.github.bon.wonx.domain.movies.dto.MovieDto;
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

    // 메인 배너 (DB에서 영화 랭크가 낮은 1개)
    public MovieDto getBannerMovie() {
        return movieRepository.findTop1ByOrderByBoxOfficeRankAsc()
                .map(MovieDto::from)
                .orElseThrow(() -> new RuntimeException("해당 영화를 찾을 수 없습니다."));
    }

    // 개봉 예정작
    public List<MovieDto> getUpcomingMovies() {
        LocalDate today = LocalDate.now();
        LocalDate weekLater = today.plusDays(7);

        return movieRepository.findByReleaseDateBetweenOrderByReleaseDateAsc(today, weekLater)
                .stream()
                .map(MovieDto::from)
                .toList();
    }

    // 조회수 기반 인기 콘텐츠
    public List<HotMovieDto> getHotMovies(int count) {
        Pageable pageable = PageRequest.of(0, count);

        return movieRepository.findAllByOrderByViewCountDesc(pageable)
                .stream()
                .map(HotMovieDto::from)
                .toList();
    }

    public List<MovieDto> findByIds(List<UUID> ids) {
        return movieRepository.findAllById(ids).stream()
                .map(MovieDto::from)
                .toList();
    }
}