package kc.mapping.codegenerate;

import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.enums.codegenerate.ModelBaseType;
import kc.enums.codegenerate.PrimaryKeyType;
import kc.framework.enums.AttributeDataType;
import kc.framework.enums.BusinessType;
import kc.model.codegenerate.ModelDefField;
import kc.model.codegenerate.ModelDefinition;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ModelDefinitionMapping {
    // ModelDefinitionMapping INSTANCE = Mappers.getMapper(ModelDefinitionMapping.class);

    @Mappings({
            @Mapping(source = "modelBaseType", target = "modelBaseType"),
            @Mapping(target = "modelCategory", ignore = true)})
    ModelDefinition toModelDefinition(ModelDefinitionDTO source);
    List<ModelDefinition> toModelDefinitionList(List<ModelDefinitionDTO> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(target = "propertyAttributeList", ignore = true),
            @Mapping(source = "modelCategory.id", target = "categoryId"),
            @Mapping(source = "modelCategory.name", target = "categoryName"),
            @Mapping(source = "modelBaseType", target = "modelBaseType"),
            @Mapping(target = "modelBaseTypeString", ignore = true)})
    @Named(value = "simpleFromModelDefinition")
    ModelDefinitionDTO simpleFromModelDefinition(ModelDefinition source);
    @IterableMapping(qualifiedByName = "simpleFromModelDefinition")
    List<ModelDefinitionDTO> simpleFromModelDefinitionList(List<ModelDefinition> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(source = "modelCategory.id", target = "categoryId"),
            @Mapping(source = "modelCategory.name", target = "categoryName"),
            @Mapping(source = "modelBaseType", target = "modelBaseType"),
            @Mapping(target = "modelBaseTypeString", ignore = true)})
    ModelDefinitionDTO fromModelDefinition(ModelDefinition source);
    List<ModelDefinitionDTO> fromModelDefinitionList(List<ModelDefinition> source);

    @Mappings({
            @Mapping(target = "modelDefinition", ignore = true),
            @Mapping(source = "dataType", target = "dataType"),
            @Mapping(source = "primaryKeyType", target = "primaryKeyType")})
    ModelDefField toModelDefField(ModelDefFieldDTO source);
    List<ModelDefField> toModelDefFieldList(List<ModelDefFieldDTO> source);

    @Mappings({
            @Mapping(target = "isEditMode", ignore = true),
            @Mapping(source = "modelDefinition.propertyId", target = "modelDefId"),
            @Mapping(source = "modelDefinition.name", target = "modelDefName"),
            @Mapping(source = "dataType", target = "dataType"),
            @Mapping(source = "primaryKeyType", target = "primaryKeyType"),
            @Mapping(target = "primaryKeyTypeString", ignore = true)})
    ModelDefFieldDTO fromModelDefField(ModelDefField source);
    List<ModelDefFieldDTO> fromModelDefFieldList(List<ModelDefField> source);


    default ModelBaseType fromModelBaseType(int type) {
        return ModelBaseType.valueOf(type);
    }
    default Integer toModelBaseType(ModelBaseType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default BusinessType fromBusinessType(int type) {
        return BusinessType.valueOf(type);
    }
    default Integer toBusinessType(BusinessType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default PrimaryKeyType fromPrimaryKeyType(int type) {
        return PrimaryKeyType.valueOf(type);
    }
    default Integer toPrimaryKeyType(PrimaryKeyType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default AttributeDataType fromAttributeDataType(int type) {
        return AttributeDataType.valueOf(type);
    }
    default Integer toAttributeDataType(AttributeDataType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
