package io.github.bon.wonx.domain.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateDto {
    @NotBlank
    private String content;
    @Min(1) @Max(10)
    private Integer rating;
    
}
