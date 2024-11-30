package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long>
{
    List<Field> findByFarmId(Long farmId);
}
