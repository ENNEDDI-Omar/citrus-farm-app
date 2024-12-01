package com.projects.citrus.dto.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class TreeRequest
{
    @NotNull(message = "Planting date is required")
    @FutureOrPresent(message = "Planting date cannot be in the past")
    private LocalDate plantingDate;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
