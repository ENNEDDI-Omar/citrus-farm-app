package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long>
{
    List<Farm> findByAreaGreaterThanEqual(Double minArea);
    List<Farm> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);
}
