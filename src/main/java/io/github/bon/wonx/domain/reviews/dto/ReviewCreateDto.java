package io.github.bon.wonx.domain.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateDto {
    @NotNull @Min(1) @Max(10)
    private Integer rating;

    @NotBlank
    private String content;

    private Boolean isAnonymous = false;
}
