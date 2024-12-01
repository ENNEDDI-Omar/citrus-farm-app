package com.projects.citrus.dto.responses;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;

@Data
@Builder
public class TreeResponse {
    private Long id;
    private LocalDate plantingDate;
    private int age;
    private double productivity;
    private boolean isProductiveAge;
    private Long fieldId;
    private String fieldName;
}
