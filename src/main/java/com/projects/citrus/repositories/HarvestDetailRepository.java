package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
    List<HarvestDetail> findByHarvestId(Long harvestId);
    List<HarvestDetail> findByTreeId(Long treeId);
    Optional<HarvestDetail> findByHarvestIdAndTreeId(Long harvestId, Long treeId);
    boolean existsByHarvestIdAndTreeId(@Param("harvestId") Long harvestId, @Param("treeId") Long treeId);

    @Query("SELECT SUM(hd.quantity) FROM HarvestDetail hd WHERE hd.harvest.id = :harvestId")
    Double calculateTotalQuantityForHarvest(Long harvestId);
}
