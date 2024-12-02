package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.Sale;
import com.projects.citrus.dto.requests.SaleRequest;
import com.projects.citrus.dto.responses.SaleResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "harvest", ignore = true)
    Sale toEntity(SaleRequest request);

    @Mapping(target = "totalRevenue", expression = "java(sale.calculateRevenue())")
    @Mapping(target = "harvestId", source = "harvest.id")
    SaleResponse toResponse(Sale sale);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSaleFromRequest(SaleRequest request, @MappingTarget Sale sale);
}
