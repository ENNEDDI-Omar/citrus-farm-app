package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.HarvestDetailRequest;
import com.projects.citrus.dto.responses.HarvestDetailResponse;
import com.projects.citrus.dto.responses.TreeResponse;

import java.util.List;

public interface IHarvestDetailService {
    HarvestDetailResponse create(Long harvestId, HarvestDetailRequest request);
    List<HarvestDetailResponse> getAllByHarvestId(Long harvestId);
    HarvestDetailResponse getById(Long id);
    void delete(Long id);
    List<TreeResponse> getAvailableTreesForHarvest(Long harvestId);
}
