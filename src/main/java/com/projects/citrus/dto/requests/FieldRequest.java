package com.projects.citrus.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FieldRequest {

    @NotBlank(message = "Name is required and cannot be null or empty")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @NotNull(message = "Area is required and cannot be null")
    @Positive(message = "Area must be positive")
    @DecimalMin(value = "0.1", message = "Field Area must be at least 0.1 hectare")
    private Double area;

    @NotNull(message = "Farm ID is required")
    private Long farmId;
}
