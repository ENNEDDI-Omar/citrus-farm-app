package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Harvest;
import com.projects.citrus.dto.requests.HarvestRequest;
import com.projects.citrus.dto.responses.HarvestResponse;
import com.projects.citrus.exceptions.BusinessException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.mapper.HarvestMapper;
import com.projects.citrus.repositories.HarvestRepository;
import com.projects.citrus.services.interfaces.IHarvestService;
import com.projects.citrus.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements IHarvestService {
    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;

    @Override
    public HarvestResponse create(HarvestRequest request) {
        // Valider la saison avec la date
        ValidationUtil.validateHarvestDate(Harvest.builder()
                .season(request.getSeason())
                .harvestDate(request.getHarvestDate())
                .build());

        // Vérifier qu'il n'y a pas déjà une récolte pour cette saison
        if (harvestRepository.existsBySeasonAndHarvestDate(request.getSeason(), request.getHarvestDate())) {
            throw new ValidationException("A harvest already exists for this season and date");
        }

        Harvest harvest = harvestMapper.toEntity(request);
        return harvestMapper.toResponse(harvestRepository.save(harvest));
    }

    @Override
    public HarvestResponse getById(Long id) {
        return harvestRepository.findById(id)
                .map(harvestMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
    }

    @Override
    public List<HarvestResponse> getAll() {
        return harvestRepository.findAll().stream()
                .map(harvestMapper::toResponse)
                .toList();
    }

    @Override
    public HarvestResponse update(Long id, HarvestRequest request) {
        Harvest existingHarvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));

        // Valider la saison avec la date
        ValidationUtil.validateHarvestDate(Harvest.builder()
                .season(request.getSeason())
                .harvestDate(request.getHarvestDate())
                .build());

        // Vérifier si la récolte a déjà des détails
        if (!existingHarvest.getHarvestDetails().isEmpty()) {
            ValidationUtil.validateHarvestUpdateWithDetails(existingHarvest, request);
        }

        harvestMapper.updateHarvestFromRequest(request, existingHarvest);
        return harvestMapper.toResponse(harvestRepository.save(existingHarvest));
    }

    @Override
    public void delete(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));

        if (!harvest.getHarvestDetails().isEmpty()) {
            throw new BusinessException("Cannot delete harvest with existing details");
        }

        if (harvest.isSold()) {
            throw new BusinessException("Cannot delete harvest that has been sold");
        }

        harvestRepository.deleteById(id);
    }

    @Override
    public List<HarvestResponse> getHarvestsByDateRange(LocalDate startDate, LocalDate endDate) {
        ValidationUtil.validateDateRange(startDate, endDate);
        return harvestRepository.findByHarvestDateBetween(startDate, endDate).stream()
                .map(harvestMapper::toResponse)
                .toList();
    }

    @Override
    public boolean isSold(Long id) {
        return harvestRepository.findById(id)
                .map(Harvest::isSold)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
    }

    @Override
    public double calculateTotalQuantity(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
        harvest.calculateTotalQuantity();
        return harvest.getTotalQuantity();
    }
}
