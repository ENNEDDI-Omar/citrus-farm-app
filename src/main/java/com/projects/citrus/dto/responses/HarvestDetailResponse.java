package com.projects.citrus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HarvestDetailResponse {
    private Long id;
    private Long treeId;
    private LocalDate treePlantingDate;  // Changé en LocalDate
    private Double quantity;
}
