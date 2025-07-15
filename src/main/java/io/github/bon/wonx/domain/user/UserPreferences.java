package io.github.bon.wonx.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.bon.wonx.domain.people.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_preferences")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "top_genres_json", columnDefinition = "json")
    private String topGenresJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_actor_id")
    private Person favoriteActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_director_id")
    private Person favoriteDirector;

    @Column(name = "active_hours_json", columnDefinition = "json")
    private String activeHoursJson;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
