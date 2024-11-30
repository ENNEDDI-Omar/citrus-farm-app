package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.FieldRequest;
import com.projects.citrus.dto.responses.FieldResponse;

import java.util.List;

public interface IFieldService
{
    FieldResponse createField(FieldRequest request);
    FieldResponse updateField(Long id, FieldRequest request);
    FieldResponse getFieldById(Long id);
    FieldResponse getFieldWithTrees(Long id);
    List<FieldResponse> getAllFields();
    List<FieldResponse> getFieldsByFarmId(Long farmId);
    void deleteField(Long id);
}
