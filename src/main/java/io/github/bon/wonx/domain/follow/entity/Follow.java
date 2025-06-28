package io.github.bon.wonx.domain.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@Getter
@NoArgsConstructor
public class Follow {

    @EmbeddedId
    private FollowId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Follow(FollowId id) {
        this.id = id;
    }
}
