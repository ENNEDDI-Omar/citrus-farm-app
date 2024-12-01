package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Field;
import com.projects.citrus.domain.entities.Tree;
import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
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
        // Validation du champ (Field)
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + request.getFieldId()));

        // Validation de la date de plantation
        ValidationUtil.validatePlantingDate(request.getPlantingDate());

        // Vérifier si le champ peut accueillir plus d'arbres
        ValidationUtil.validateTreeAddition(field, 1);

        // Création de l'arbre
        Tree tree = treeMapper.toEntity(request);
        tree.setField(field);

        return treeMapper.toResponse(treeRepository.save(tree));
    }

    @Override
    public TreeResponse update(Long id, TreeRequest request) {
        // Récupérer l'arbre existant
        Tree existingTree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));

        // Si changement de champ
        if (!existingTree.getField().getId().equals(request.getFieldId())) {
            Field newField = fieldRepository.findById(request.getFieldId())
                    .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + request.getFieldId()));

            // Vérifier si le nouveau champ peut accueillir l'arbre
            ValidationUtil.validateTreeAddition(newField, 1);
            existingTree.setField(newField);
        }

        // Si changement de date de plantation
        if (request.getPlantingDate() != null && !request.getPlantingDate().equals(existingTree.getPlantingDate())) {
            ValidationUtil.validatePlantingDate(request.getPlantingDate());
        }

        treeMapper.updateTreeFromRequest(request, existingTree);
        return treeMapper.toResponse(treeRepository.save(existingTree));
    }

    @Override
    public TreeResponse getById(Long id) {
        return null;
    }

    @Override
    public List<TreeResponse> getAll() {
        return List.of();
    }

    @Override
    public List<TreeResponse> getByFieldId(Long fieldId) {
        return List.of();
    }

    @Override
    public long getTreeCountInField(Long fieldId) {
        return 0;
    }

    @Override
    public void delete(Long id) {

    }
}
