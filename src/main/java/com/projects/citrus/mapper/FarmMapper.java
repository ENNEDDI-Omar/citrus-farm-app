package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.Farm;
import com.projects.citrus.dto.requests.FarmRequest;
import com.projects.citrus.dto.responses.FarmResponse;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {FieldMapper.class}, imports = {LocalDate.class})
public interface FarmMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fields", ignore = true)
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    Farm toEntity(FarmRequest request);

    @Mapping(target = "remainingArea", expression = "java(farm.calculateLeftArea())")
    FarmResponse toResponse(Farm farm);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFarmFromRequest(FarmRequest request, @MappingTarget Farm farm);
}
