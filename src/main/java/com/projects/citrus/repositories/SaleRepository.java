package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByHarvestId(Long harvestId);

    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(s.unitPrice * s.harvest.totalQuantity) FROM Sale s WHERE s.harvest.id = :harvestId")
    Double calculateTotalRevenueForHarvest(@Param("harvestId") Long harvestId);

    boolean existsByHarvestId(Long harvestId);
}
