package com.projects.citrus.dto.responses;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class FieldResponse {
    private Long id;
    private Double area;
    private int maxTreeCapacity;
    private int currentTreeCount;
    private List<TreeResponse> trees;
}
