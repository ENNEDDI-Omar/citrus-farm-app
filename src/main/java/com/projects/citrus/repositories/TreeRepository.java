package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Tree;

import java.util.List;

public interface TreeRepository
{
    List<Tree> findByFieldId(Long fieldId);
}
