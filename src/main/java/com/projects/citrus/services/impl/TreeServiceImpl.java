package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Field;
import com.projects.citrus.domain.entities.Tree;
import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.services.interfaces.ITreeService;
import com.projects.citrus.repositories.FieldRepository;
import com.projects.citrus.repositories.TreeRepository;
import com.projects.citrus.mapper.TreeMapper;
import lombok.RequiredArgsConstructor;
import com.projects.citrus.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreeServiceImpl implements ITreeService {
    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final TreeMapper treeMapper;

    @Override
    public TreeResponse create(TreeRequest request) {

        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + request.getFieldId()));

        ValidationUtil.validatePlantingDate(request.getPlantingDate());

        long currentTreeCount = treeRepository.countByFieldId(field.getId());
        if (currentTreeCount >= ValidationUtil.getMaxTreeCapacityForField(field.getArea())) {
            throw new ValidationException(
                    String.format("Field has reached its maximum capacity of %d trees",
                            ValidationUtil.getMaxTreeCapacityForField(field.getArea()))
            );
        }

        Tree tree = treeMapper.toEntity(request);
        tree.setField(field);

        return treeMapper.toResponse(treeRepository.save(tree));
    }

    @Override
    public TreeResponse update(Long id, TreeRequest request) {

        Tree existingTree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));


        if (!existingTree.getField().getId().equals(request.getFieldId())) {
            Field newField = fieldRepository.findById(request.getFieldId())
                    .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + request.getFieldId()));


            long currentTreeCount = treeRepository.countByFieldId(newField.getId());
            ValidationUtil.validateTreeAddition(newField, (int)currentTreeCount + 1);

            existingTree.setField(newField);
        }

        if (request.getPlantingDate() != null && !request.getPlantingDate().equals(existingTree.getPlantingDate())) {
            ValidationUtil.validatePlantingDate(request.getPlantingDate());
        }

        treeMapper.updateTreeFromRequest(request, existingTree);
        return treeMapper.toResponse(treeRepository.save(existingTree));
    }

    public TreeResponse getById(Long id) {
        return treeRepository.findById(id)
                .map(treeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));
    }

    @Override
    public List<TreeResponse> getAll() {
        return treeRepository.findAll().stream()
                .map(treeMapper::toResponse)
                .toList();
    }

    @Override
    public List<TreeResponse> getByFieldId(Long fieldId) {
        if (!fieldRepository.existsById(fieldId)) {
            throw new ResourceNotFoundException("Field not found with id: " + fieldId);
        }
        return treeRepository.findByFieldId(fieldId).stream()
                .map(treeMapper::toResponse)
                .toList();
    }

    @Override
    public long getTreeCountInField(Long fieldId) {
        if (!fieldRepository.existsById(fieldId)) {
            throw new ResourceNotFoundException("Field not found with id: " + fieldId);
        }
        return treeRepository.countByFieldId(fieldId);
    }

    @Override
    public void delete(Long id) {
        if (!treeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tree not found with id: " + id);
        }
        treeRepository.deleteById(id);
    }
}
