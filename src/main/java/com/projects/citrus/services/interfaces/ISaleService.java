package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.SaleRequest;
import com.projects.citrus.dto.responses.SaleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {
    SaleResponse create(Long harvestId, SaleRequest request);
    SaleResponse getById(Long id);
    List<SaleResponse> getAll();
    List<SaleResponse> getByHarvestId(Long harvestId);
    List<SaleResponse> getBySaleDateRange(LocalDate startDate, LocalDate endDate);
    Double calculateTotalRevenue(Long harvestId);
    SaleResponse update(Long id, SaleRequest request);
    void delete(Long id);
}
