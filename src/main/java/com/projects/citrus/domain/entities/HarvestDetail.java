package com.projects.citrus.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "harvest_details" , uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_harvest_detail_tree_harvest",
                columnNames = {"harvest_id", "tree_id"}
        )
})
public class HarvestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id")
    private Tree tree;

    private Double quantity;
}
