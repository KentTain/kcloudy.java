package kc.mapping.offering;

import kc.mapping.CycleAvoidingMappingContext;
import kc.model.offering.*;
import kc.dto.offering.*;
import kc.enums.offering.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.EnumSet;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CategoryManagerMapping.class, PropertyProviderMapping.class},
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapping {
    CategoryMapping INSTANCE = Mappers.getMapper(CategoryMapping.class);

    @Mappings({
            @Mapping(source = "text", target = "name"),
            @Mapping(source = "children", target = "childNodes"),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "categoryOperationLogs", ignore = true),
            @Mapping(target = "offerings", ignore = true)
    })
    Category to(CategoryDTO source, @Context CycleAvoidingMappingContext context);

    List<Category> to(List<CategoryDTO> source, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(source = "name", target = "text"),
            @Mapping(source = "parentNode.id", target = "parentId"),
            @Mapping(source = "parentNode.name", target = "parentName"),
            @Mapping(source = "offeringPriceType", target = "offeringPriceType"),
            @Mapping(source = "offeringPropertyType", target = "offeringPropertyType"),
            @Mapping(source = "offeringPropertyType", target = "offeringPropertyTypes"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "parentNode", ignore = true),
            @Mapping(target = "offerings", ignore = true)
    })
    @InheritInverseConfiguration
    CategoryDTO from(Category source, @Context CycleAvoidingMappingContext context);

    List<CategoryDTO> from(List<Category> source, @Context CycleAvoidingMappingContext context);

    default OfferingPriceType ConvertOfferingPriceType(int type) {
        return OfferingPriceType.valueOf(type);
    }
    default int ConvertOfferingPriceType(OfferingPriceType type) {
        return type.getIndex();
    }

    default OfferingPropertyType ConvertOfferingPropertyType(int type) { return OfferingPropertyType.valueOf(type); }
    default int ConvertOfferingPropertyType(OfferingPropertyType type) { return type.getIndex(); }

    default EnumSet<OfferingPropertyType> ConvertOfferingPropertyTypes(int type) {
        EnumSet<OfferingPropertyType> result = EnumSet.noneOf(OfferingPropertyType.class);
        if((type & OfferingPropertyType.Detail.getIndex()) > 0){
            result.add(OfferingPropertyType.Detail);
        }
        if((type & OfferingPropertyType.Image.getIndex()) > 0){
            result.add(OfferingPropertyType.Image);
        }
        if((type & OfferingPropertyType.File.getIndex()) > 0){
            result.add(OfferingPropertyType.File);
        }
        if((type & OfferingPropertyType.Video.getIndex()) > 0){
            result.add(OfferingPropertyType.Video);
        }
        if((type & OfferingPropertyType.Audio.getIndex()) > 0){
            result.add(OfferingPropertyType.Audio);
        }
        if((type & OfferingPropertyType.Area.getIndex()) > 0){
            result.add(OfferingPropertyType.Area);
        }
        if((type & OfferingPropertyType.ServiceProvider.getIndex()) > 0){
            result.add(OfferingPropertyType.ServiceProvider);
        }
        if((type & OfferingPropertyType.PaymentInfo.getIndex()) > 0){
            result.add(OfferingPropertyType.PaymentInfo);
        }

        return result;
    }
    default int ConvertOfferingPropertyTypes(EnumSet<OfferingPropertyType> types) {
        int v = 0;
        for (OfferingPropertyType version : types) {
            v = v | version.getIndex();
        }
        return v;
    }


    default OfferingType ConvertOfferingType(int type) {
        return OfferingType.valueOf(type);
    }
    default int ConvertOfferingType(OfferingType type) {
        return type.getIndex();
    }

    default OfferingStatus ConvertOfferingStatus(int type) {
        return OfferingStatus.valueOf(type);
    }
    default int ConvertOfferingStatus(OfferingStatus type) {
        return type.getIndex();
    }
}
