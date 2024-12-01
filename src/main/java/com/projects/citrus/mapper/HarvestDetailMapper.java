package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.HarvestDetail;
import com.projects.citrus.dto.requests.HarvestDetailRequest;
import com.projects.citrus.dto.responses.HarvestDetailResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HarvestDetailMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "harvest", ignore = true)
    @Mapping(target = "tree", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    HarvestDetail toEntity(HarvestDetailRequest request);

    @Mapping(target = "treeId", source = "tree.id")
    @Mapping(target = "treePlantingDate", source = "tree.plantingDate")
    HarvestDetailResponse toResponse(HarvestDetail harvestDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHarvestDetailFromRequest(HarvestDetailRequest request, @MappingTarget HarvestDetail harvestDetail);
}
