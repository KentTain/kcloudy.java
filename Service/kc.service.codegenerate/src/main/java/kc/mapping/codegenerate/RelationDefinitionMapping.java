package kc.mapping.codegenerate;

import kc.dto.codegenerate.RelationDefDetailDTO;
import kc.dto.codegenerate.RelationDefinitionDTO;
import kc.enums.codegenerate.RelationType;
import kc.model.codegenerate.RelationDefDetail;
import kc.model.codegenerate.RelationDefinition;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface RelationDefinitionMapping {
    // RelationDefinitionMapping INSTANCE = Mappers.getMapper(RelationDefinitionMapping.class);

    @Mappings({
            @Mapping(target = "modelCategory", ignore = true)})
    RelationDefinition toRelationDefinition(RelationDefinitionDTO source);
    List<RelationDefinition> toRelationDefinitionList(List<RelationDefinitionDTO> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(target = "defDetails", ignore = true),
            @Mapping(target = "mainModelDef", ignore = true),
            @Mapping(source = "modelCategory.id", target = "categoryId"),
            @Mapping(source = "modelCategory.name", target = "categoryName")})
    @Named(value = "simpleFromRelationDefinition")
    RelationDefinitionDTO simpleFromRelationDefinition(RelationDefinition source);
    @IterableMapping(qualifiedByName = "simpleFromRelationDefinition")
    List<RelationDefinitionDTO> simpleFromRelationDefinitionList(List<RelationDefinition> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(target = "mainModelDef", ignore = true),
            @Mapping(source = "modelCategory.id", target = "categoryId"),
            @Mapping(source = "modelCategory.name", target = "categoryName")})
    RelationDefinitionDTO fromRelationDefinition(RelationDefinition source);
    List<RelationDefinitionDTO> fromRelationDefinitionList(List<RelationDefinition> source);

    @Mappings({
            @Mapping(target = "relationDefinition", ignore = true),
            @Mapping(source = "relationType", target = "relationType")})
    RelationDefDetail toRelationDefDetail(RelationDefDetailDTO source);
    List<RelationDefDetail> toRelationDefDetailList(List<RelationDefDetailDTO> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(source = "relationDefinition.id", target = "relationDefId"),
            @Mapping(source = "relationDefinition.name", target = "relationDefName"),
            @Mapping(source = "relationType", target = "relationType"),
            @Mapping(target = "mainModelDefField", ignore = true),
            @Mapping(target = "subModelDef", ignore = true),
            @Mapping(target = "subModelDefField", ignore = true),})
    RelationDefDetailDTO fromRelationDefDetail(RelationDefDetail source);
    List<RelationDefDetailDTO> fromRelationDefDetailList(List<RelationDefDetail> source);


    default RelationType fromRelationType(int type) {
        return RelationType.valueOf(type);
    }
    default Integer toRelationType(RelationType type) {
        if (null == type) return null;
        return type.getIndex();
    }

}
