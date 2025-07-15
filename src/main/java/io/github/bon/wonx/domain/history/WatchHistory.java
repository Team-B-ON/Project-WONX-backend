package io.github.bon.wonx.domain.history;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.movies.entity.Movie;
import io.github.bon.wonx.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name="watch_histories")
public class WatchHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="video_id", nullable=false)
    private Movie movie;

    @Column(name="watched_at", nullable=false)
    private LocalDateTime watchedAt;

    @Column(name="last_position")
    private Integer lastPosition;

    @Column(name="watched_seconds")
    private Integer watchedSeconds;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="is_completed")
    private Boolean isCompleted;
}
