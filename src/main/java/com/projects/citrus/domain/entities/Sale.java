package com.projects.citrus.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate saleDate;
    private Double unitPrice;
    private String client;

    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;

    public double calculateRevenue()
    {
        return unitPrice * harvest.getTotalQuantity();
    }
}
