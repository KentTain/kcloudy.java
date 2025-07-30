package kc.mapping.offering;

import kc.dto.offering.PropertyProviderAttrDTO;
import kc.dto.offering.PropertyProviderDTO;
import kc.enums.offering.*;
import kc.framework.enums.PropertyDataType;
import kc.model.offering.PropertyProvider;
import kc.model.offering.PropertyProviderAttr;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface PropertyProviderMapping {
    PropertyProviderMapping INSTANCE = Mappers.getMapper(PropertyProviderMapping.class);

    @Mappings({
            @Mapping(source = "serviceDataType", target = "serviceDataType"),
            @Mapping(target = "category", ignore = true)
    })
    PropertyProvider to(PropertyProviderDTO source);
    List<PropertyProvider> to(List<PropertyProviderDTO> source);

    @Mappings({
            @Mapping(source = "serviceDataType", target = "serviceDataType"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "serviceDataTypeString", ignore = true),
            @Mapping(target = "category", ignore = true)
    })
    PropertyProviderDTO from(PropertyProvider source);
    List<PropertyProviderDTO> from(List<PropertyProvider> source);


    @Mappings({
            @Mapping(source = "serviceAttrDataType", target = "serviceAttrDataType"),
            @Mapping(target = "serviceProvider", ignore = true),
    })
    PropertyProviderAttr toAttr(PropertyProviderAttrDTO source);
    List<PropertyProviderAttr> toAttr(List<PropertyProviderAttrDTO> source);

    @Mappings({
            @Mapping(source = "serviceAttrDataType", target = "serviceAttrDataType"),
            @Mapping(source = "serviceProvider.id", target = "serviceProviderId"),
            @Mapping(source = "serviceProvider.name", target = "serviceProviderName"),
            @Mapping(target = "serviceAttrDataTypeString", ignore = true),
    })
    PropertyProviderAttrDTO fromAttr(PropertyProviderAttr source);
    List<PropertyProviderAttrDTO> fromAttr(List<PropertyProviderAttr> source);


    default ServiceDataType ConvertServiceDataType(int type) {
        return ServiceDataType.valueOf(type);
    }
    default int ConvertServiceDataType(ServiceDataType type) { return type.getIndex(); }

    default ServiceAttrDataType ConvertServiceAttrDataType(int type) {
        return ServiceAttrDataType.valueOf(type);
    }
    default int ConvertServiceAttrDataType(ServiceAttrDataType type) { return type.getIndex(); }

}
