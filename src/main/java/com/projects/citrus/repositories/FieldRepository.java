package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long>
{
    List<Field> findByFarmId(Long farmId);
    boolean existsByName(String name);
    Optional<Field> findByNameAndIdNot(String name, Long id);
}
