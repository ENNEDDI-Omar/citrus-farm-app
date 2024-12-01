package com.projects.citrus.dto.requests;

import com.projects.citrus.domain.enums.Season;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HarvestRequest {
    @NotNull(message = "Season is required")
    private Season season;

    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;
}
