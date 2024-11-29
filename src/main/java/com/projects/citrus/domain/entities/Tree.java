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
@Table(name = "trees")
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate plantingDate;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL)
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    public int getAge() {
        return LocalDate.now().getYear() - plantingDate.getYear();
    }

    public double getProductivity() {
        int age = getAge();
        if (age < 3) return 2.5;
        if (age <= 10) return 12.0;
        if (age <= 20) return 20.0;
        return 0.0;
    }
}
