package com.projects.citrus.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HarvestDetailResponse {
    private Long id;
    private Long treeId;
    private String treePlantingDate;
    private Double quantity;
}
