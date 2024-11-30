package com.projects.citrus.services.impl;

import com.projects.citrus.dto.requests.FieldRequest;
import com.projects.citrus.dto.responses.FieldResponse;
import com.projects.citrus.services.interfaces.IFieldService;

import java.util.List;

public class FieldServiceImpl implements IFieldService
{

    @Override
    public FieldResponse createField(FieldRequest request) {
        return null;
    }

    @Override
    public FieldResponse updateField(Long id, FieldRequest request) {
        return null;
    }

    @Override
    public FieldResponse getFieldById(Long id) {
        return null;
    }

    @Override
    public FieldResponse getFieldWithTrees(Long id) {
        return null;
    }

    @Override
    public List<FieldResponse> getAllFields() {
        return List.of();
    }


    @Override
    public void deleteField(Long id) {

    }

    @Override
    public List<FieldResponse> getFieldsByFarmId(Long farmId) {
        return List.of();
    }
}
