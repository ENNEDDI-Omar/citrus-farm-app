package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.HarvestRequest;
import com.projects.citrus.dto.responses.HarvestResponse;

import java.time.LocalDate;
import java.util.List;

public interface IHarvestService
{
    HarvestResponse create(HarvestRequest request);
    HarvestResponse getById(Long id);
    List<HarvestResponse> getAll();
    HarvestResponse update(Long id, HarvestRequest request);
    void delete(Long id);
    List<HarvestResponse> getHarvestsByDateRange(LocalDate startDate, LocalDate endDate);
    boolean isSold(Long id);
    double calculateTotalQuantity(Long id);
}
