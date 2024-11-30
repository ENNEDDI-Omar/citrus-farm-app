package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.Field;
import com.projects.citrus.dto.requests.FieldRequest;
import com.projects.citrus.dto.responses.FieldResponse;
import com.projects.citrus.util.ValidationUtil;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TreeMapper.class}, imports = {ValidationUtil.class})
public interface FieldMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "farm", ignore = true)
    @Mapping(target = "trees", ignore = true)
    Field toEntity(FieldRequest request);

    @Mapping(target = "maxTreeCapacity", expression = "java(ValidationUtil.getMaxTreeCapacityForField(field.getArea()))")
    @Mapping(target = "currentTreeCount", expression = "java(field.getTrees() != null ? field.getTrees().size() : 0)")
    @Mapping(target = "farmId", source = "farm.id")
    @Mapping(target = "farmName", source = "farm.name")
    FieldResponse toResponse(Field field);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFieldFromRequest(FieldRequest request, @MappingTarget Field field);
}
