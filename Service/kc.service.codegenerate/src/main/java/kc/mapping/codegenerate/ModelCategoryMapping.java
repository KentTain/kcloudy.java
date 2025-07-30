package kc.mapping.codegenerate;

import kc.dto.codegenerate.ModelCategoryDTO;
import kc.enums.codegenerate.ModelType;
import kc.framework.enums.ProcessLogType;
import kc.mapping.CycleAvoidingMappingContext;
import kc.model.codegenerate.ModelCategory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ModelCategoryMapping {
    // ModelCategoryMapping INSTANCE = Mappers.getMapper(ModelCategoryMapping.class);

    @Mappings({
            @Mapping(source = "modelType", target = "modelType"),
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            @Mapping(target = "parentNode", ignore = true),
    })
    ModelCategory to(ModelCategoryDTO source, @Context CycleAvoidingMappingContext context);
    List<ModelCategory> to(List<ModelCategoryDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "childNodes", target = "children"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "modelType", target = "modelType"),
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(target = "modelTypeString", ignore = true)})
    ModelCategoryDTO from(ModelCategory source, @Context CycleAvoidingMappingContext context);
    List<ModelCategoryDTO> from(List<ModelCategory> source, @Context CycleAvoidingMappingContext context);

    default ModelType toModelType(int type) {
        return ModelType.valueOf(type);
    }
    default Integer fromModelType(ModelType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
