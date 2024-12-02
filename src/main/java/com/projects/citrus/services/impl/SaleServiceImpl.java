package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Harvest;
import com.projects.citrus.domain.entities.Sale;
import com.projects.citrus.dto.requests.SaleRequest;
import com.projects.citrus.dto.responses.SaleResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.mapper.SaleMapper;
import com.projects.citrus.repositories.HarvestRepository;
import com.projects.citrus.repositories.SaleRepository;
import com.projects.citrus.services.interfaces.ISaleService;
import com.projects.citrus.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements ISaleService {
    private final SaleRepository saleRepository;
    private final HarvestRepository harvestRepository;
    private final SaleMapper saleMapper;

    @Override
    public SaleResponse create(Long harvestId, SaleRequest request) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + harvestId));

        // Vérifier que la récolte a une quantité à vendre
        if (harvest.getTotalQuantity() <= 0) {
            throw new ValidationException("Cannot create sale for harvest with no quantity");
        }

        Sale sale = saleMapper.toEntity(request);
        sale.setHarvest(harvest);

        // Valider la vente
        ValidationUtil.validateSale(sale);

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
    }

    @Override
    public List<SaleResponse> getAll() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    public List<SaleResponse> getByHarvestId(Long harvestId) {
        if (!harvestRepository.existsById(harvestId)) {
            throw new ResourceNotFoundException("Harvest not found with id: " + harvestId);
        }
        return saleRepository.findByHarvestId(harvestId).stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    public List<SaleResponse> getBySaleDateRange(LocalDate startDate, LocalDate endDate) {
        ValidationUtil.validateDateRange(startDate, endDate);
        return saleRepository.findBySaleDateBetween(startDate, endDate).stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    public Double calculateTotalRevenue(Long harvestId) {
        if (!harvestRepository.existsById(harvestId)) {
            throw new ResourceNotFoundException("Harvest not found with id: " + harvestId);
        }
        return saleRepository.calculateTotalRevenueForHarvest(harvestId);
    }

    @Override
    public SaleResponse update(Long id, SaleRequest request) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        saleMapper.updateSaleFromRequest(request, sale);
        ValidationUtil.validateSale(sale);

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Override
    public void delete(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sale not found with id: " + id);
        }
        saleRepository.deleteById(id);
    }
}
