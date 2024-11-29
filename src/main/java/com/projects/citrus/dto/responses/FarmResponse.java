package com.projects.citrus.dto.responses;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FarmResponse {
    private Long id;
    private String name;
    private String location;
    private Double area;
    private LocalDate creationDate;
    private Double remainingArea;
    private List<FieldResponse> fields;
}
