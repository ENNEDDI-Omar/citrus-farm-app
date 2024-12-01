package com.projects.citrus.dto.responses;

import com.projects.citrus.domain.enums.Season;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class HarvestResponse {
    private Long id;
    private Season season;
    private LocalDate harvestDate;
    private Double totalQuantity;
    private List<HarvestDetailResponse> details;
    private boolean sold;
}
