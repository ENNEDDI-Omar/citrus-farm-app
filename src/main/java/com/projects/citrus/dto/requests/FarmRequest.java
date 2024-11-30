package com.projects.citrus.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FarmRequest
{
    @NotBlank(message = "Farm name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    private Double area;
}
