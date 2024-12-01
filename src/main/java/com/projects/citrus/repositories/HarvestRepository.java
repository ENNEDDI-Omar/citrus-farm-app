package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Harvest;
import com.projects.citrus.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    Optional<Harvest> findBySeasonAndHarvestDateBetween(
            Season season,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Harvest> findByHarvestDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsBySeasonAndHarvestDate(Season season, LocalDate harvestDate);
}
