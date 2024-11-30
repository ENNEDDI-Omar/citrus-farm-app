package com.projects.citrus.dto.responses;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class FieldResponse {
    private Long id;
    private String name;
    private Double area;
    private List<TreeResponse> trees;
    private int maxTreeCapacity;
    private int currentTreeCount;
}
