// package io.github.bon.wonx.domain.search.dto;

// import java.util.UUID;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;

// @Getter
// @Builder
// @AllArgsConstructor
// public class ReviewSearchDto {

// private UUID id;
// private String content;
// private UUID movieId;
// private UUID userId;

// public static ReviewSearchDto from(Review review) {
// return ReviewSearchDto.builder()
// .id(review.getId())
// .content(review.getContent())
// .movieId(review.getMovie().getId())
// .userId(review.getUser().getId())
// .build();
// }
// }
