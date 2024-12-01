package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.Harvest;
import com.projects.citrus.dto.requests.HarvestRequest;
import com.projects.citrus.dto.responses.HarvestResponse;
import org.mapstruct.*;


import java.util.ArrayList;

@Mapper(componentModel = "spring", uses = {HarvestDetailMapper.class}, imports = {ArrayList.class})
public interface HarvestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "totalQuantity", constant = "0.0")
    Harvest toEntity(HarvestRequest request);

    @Mapping(target = "details", source = "harvestDetails")
    @Mapping(target = "sold", expression = "java(harvest.isSold())")
    HarvestResponse toResponse(Harvest harvest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHarvestFromRequest(HarvestRequest request, @MappingTarget Harvest harvest);
}
