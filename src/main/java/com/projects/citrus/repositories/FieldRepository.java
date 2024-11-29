package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Field;

import java.util.List;

public interface FieldRepository
{
    List<Field> findByFarmId(Long farmId);
}
