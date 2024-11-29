package com.projects.citrus.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "farms")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Double area;
    private LocalDate creationDate;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Field> fields = new ArrayList<>();

    public double calculateLeftArea() {
        double currentTotalArea = fields.stream()
                .mapToDouble(Field::getArea)
                .sum();

        return this.area - currentTotalArea;
    }
}
