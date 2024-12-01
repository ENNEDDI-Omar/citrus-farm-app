package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Farm;
import com.projects.citrus.domain.entities.Field;
import com.projects.citrus.dto.requests.FieldRequest;
import com.projects.citrus.dto.responses.FieldResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.mapper.FieldMapper;
import com.projects.citrus.repositories.FarmRepository;
import com.projects.citrus.repositories.FieldRepository;
import com.projects.citrus.services.interfaces.IFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.projects.citrus.util.ValidationUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements IFieldService
{
    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
    private final FieldMapper fieldMapper;

    @Override
    public FieldResponse createField(FieldRequest request) {

        ValidationUtil.validateFieldName(request.getName(), fieldRepository);

        Farm farm = farmRepository.findById(request.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + request.getFarmId()));

        Field field = fieldMapper.toEntity(request);
        field.setFarm(farm);

        ValidationUtil.validateFieldForFarm(farm, field);

        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Override
    public FieldResponse updateField(Long id, FieldRequest request)
    {
        Field existingField = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));


        if (!existingField.getName().equals(request.getName())) {
            ValidationUtil.validateFieldNameUpdate(request.getName(), id, fieldRepository);
        }

        if (!existingField.getArea().equals(request.getArea())) {
            ValidationUtil.validateFieldAreaUpdate(existingField, request.getArea());
        }

        if (!existingField.getFarm().getId().equals(request.getFarmId())) {
            Farm newFarm = farmRepository.findById(request.getFarmId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + request.getFarmId()));

            Field tempField = Field.builder()
                    .area(request.getArea())
                    .name(request.getName())
                    .build();

            ValidationUtil.validateFieldForFarm(newFarm, tempField);
            existingField.setFarm(newFarm);
        }

        fieldMapper.updateFieldFromRequest(request, existingField);
        return fieldMapper.toResponse(fieldRepository.save(existingField));
    }

    @Override
    public FieldResponse getFieldById(Long id) {
        return fieldRepository.findById(id)
                .map(fieldMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));
    }

    @Override
    public FieldResponse getFieldWithTrees(Long id) {
        return fieldRepository.findById(id)
                .map(fieldMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));
    }

    @Override
    public List<FieldResponse> getAllFields() {
        return fieldRepository.findAll().stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    @Override
    public List<FieldResponse> getFieldsByFarmId(Long farmId) {
        if (!farmRepository.existsById(farmId)) {
            throw new ResourceNotFoundException("Farm not found with id: " + farmId);
        }
        return fieldRepository.findByFarmId(farmId).stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteField(Long id) {
        if (!fieldRepository.existsById(id)) {
            throw new ResourceNotFoundException("Field not found with id: " + id);
        }
        fieldRepository.deleteById(id);
    }
}
