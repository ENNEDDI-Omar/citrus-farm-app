package com.projects.citrus.domain.entities;

import com.projects.citrus.domain.enums.Season;
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
@Table(name = "harvests")
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Season season;
    private LocalDate harvestDate;
    private Double totalQuantity;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL)
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL)
    private List<Sale> sales = new ArrayList<>();

    public void calculateTotalQuantity() {
        this.totalQuantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
    }

    public boolean isSold() {
        return sales != null;
    }
}
