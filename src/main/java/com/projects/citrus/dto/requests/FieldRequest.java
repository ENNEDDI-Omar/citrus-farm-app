package com.projects.citrus.dto.requests;

import lombok.Data;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
public class FieldRequest {

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    private Double area;

    @NotNull(message = "Farm ID is required")
    private Long farmId;
}
