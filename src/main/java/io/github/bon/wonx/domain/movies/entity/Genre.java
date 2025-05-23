package io.github.bon.wonx.domain.movies.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genres")

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

  @Id
  @GeneratedValue
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column
  private String name;

  @ManyToMany(mappedBy = "genres")
  private List<Movie> movies;

  // 추천 기능 테스트용 단일 생성자 -> 추후 삭제 가능
  public Genre(String name) {
    this.name = name;
  }
}
