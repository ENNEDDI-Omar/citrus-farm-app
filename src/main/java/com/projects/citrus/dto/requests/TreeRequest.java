package com.projects.citrus.dto.requests;

import lombok.Data;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class TreeRequest {
    @NotNull(message = "Planting date is required")
    private LocalDate plantingDate;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
