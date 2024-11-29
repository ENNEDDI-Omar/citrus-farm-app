package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.FarmRequest;
import com.projects.citrus.dto.responses.FarmResponse;

import java.time.LocalDate;
import java.util.List;

public interface IFarmService
{
    FarmResponse create(FarmRequest request);
    List<FarmResponse> getAll();
    FarmResponse getById(Long id);
    FarmResponse update(Long id, FarmRequest request);
    void delete(Long id);

    // Nouvelles méthodes
    Double calculateLeftArea(Long id);
    List<FarmResponse> getByMinArea(Double minArea);
    List<FarmResponse> getByDateRange(LocalDate startDate, LocalDate endDate);
}
