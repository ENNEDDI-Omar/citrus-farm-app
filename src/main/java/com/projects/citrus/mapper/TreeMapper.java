package com.projects.citrus.mapper;

import com.projects.citrus.domain.entities.Tree;
import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.util.ValidationUtil;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {ValidationUtil.class})
public interface TreeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "field", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    Tree toEntity(TreeRequest request);

    @Mapping(target = "age", expression = "java(tree.getAge())")
    @Mapping(target = "productivity", expression = "java(tree.getProductivity())")
    @Mapping(target = "isProductiveAge", expression = "java(ValidationUtil.isTreeProductiveAge(tree))")
    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "fieldName", source = "field.name")
    TreeResponse toResponse(Tree tree);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTreeFromRequest(TreeRequest request, @MappingTarget Tree tree);
}
