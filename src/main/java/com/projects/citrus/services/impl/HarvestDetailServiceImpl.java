package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Harvest;
import com.projects.citrus.domain.entities.Tree;
import com.projects.citrus.domain.entities.HarvestDetail;
import com.projects.citrus.dto.requests.HarvestDetailRequest;
import com.projects.citrus.dto.responses.HarvestDetailResponse;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.exceptions.BusinessException;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.mapper.HarvestDetailMapper;
import com.projects.citrus.mapper.TreeMapper;
import com.projects.citrus.repositories.HarvestDetailRepository;
import com.projects.citrus.repositories.HarvestRepository;
import com.projects.citrus.repositories.TreeRepository;
import com.projects.citrus.services.interfaces.IHarvestDetailService;
import com.projects.citrus.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestDetailServiceImpl implements IHarvestDetailService {
    private final HarvestDetailRepository harvestDetailRepository;
    private final HarvestRepository harvestRepository;
    private final TreeRepository treeRepository;
    private final HarvestDetailMapper harvestDetailMapper;
    private final TreeMapper treeMapper;

    @Override
    public HarvestDetailResponse create(Long harvestId, HarvestDetailRequest request) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + harvestId));

        Tree tree = treeRepository.findById(request.getTreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + request.getTreeId()));

        // Vérifier si l'arbre peut être récolté
        ValidationUtil.validateTreeForHarvest(tree, harvest);

        // Vérifier si l'arbre n'a pas déjà été récolté pour cette récolte
        if (harvestDetailRepository.existsByHarvestIdAndTreeId(harvestId, request.getTreeId())) {
            throw new ValidationException("Tree has already been harvested in this harvest");
        }

        // Créer le détail de récolte avec la quantité calculée
        HarvestDetail harvestDetail = harvestDetailMapper.toEntity(request);
        harvestDetail.setHarvest(harvest);
        harvestDetail.setTree(tree);
        harvestDetail.setQuantity(tree.getProductivity());  // Utilise la productivité calculée

        // Sauvegarder et mettre à jour la quantité totale de la récolte
        HarvestDetail savedDetail = harvestDetailRepository.save(harvestDetail);
        harvest.calculateTotalQuantity();
        harvestRepository.save(harvest);

        return harvestDetailMapper.toResponse(savedDetail);
    }

    @Override
    public List<HarvestDetailResponse> getAllByHarvestId(Long harvestId) {
        if (!harvestRepository.existsById(harvestId)) {
            throw new ResourceNotFoundException("Harvest not found with id: " + harvestId);
        }
        return harvestDetailRepository.findByHarvestId(harvestId).stream()
                .map(harvestDetailMapper::toResponse)
                .toList();
    }

    @Override
    public HarvestDetailResponse getById(Long id) {
        return harvestDetailRepository.findById(id)
                .map(harvestDetailMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest detail not found with id: " + id));
    }

    @Override
    public List<TreeResponse> getAvailableTreesForHarvest(Long harvestId) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + harvestId));

        // Récupérer tous les arbres déjà récoltés dans cette récolte
        List<Long> harvestedTreeIds = harvestDetailRepository.findByHarvestId(harvestId)
                .stream()
                .map(detail -> detail.getTree().getId())
                .toList();

        // Récupérer tous les arbres disponibles (non récoltés)
        return treeRepository.findAll().stream()
                .filter(tree -> !harvestedTreeIds.contains(tree.getId()))
                .filter(tree -> tree.getAge() <= 20)  // Seulement les arbres productifs
                .map(treeMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest detail not found with id: " + id));

        // Vérifier si la récolte n'est pas déjà vendue
        if (harvestDetail.getHarvest().isSold()) {
            throw new BusinessException("Cannot delete detail from a sold harvest");
        }

        harvestDetailRepository.deleteById(id);

        // Recalculer la quantité totale de la récolte
        Harvest harvest = harvestDetail.getHarvest();
        harvest.calculateTotalQuantity();
        harvestRepository.save(harvest);
    }
}
