package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.HarvestDetail;
import com.projects.citrus.dto.responses.HarvestDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HarvestDetailMapper {
    @Mapping(target = "treeId", source = "tree.id")
    @Mapping(target = "treePlantingDate", source = "tree.plantingDate")
    HarvestDetailResponse toResponse(HarvestDetail harvestDetail);
}
