package com.projects.citrus.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FarmRequest
{
    @NotBlank(message = "Farm name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    private Double area;
}
