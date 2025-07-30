package kc.mapping.offering;

import kc.dto.offering.OfferingPropertyDTO;
import kc.enums.offering.*;
import kc.model.offering.OfferingProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface OfferingPropertyMapping {
    OfferingPropertyMapping INSTANCE = Mappers.getMapper(OfferingPropertyMapping.class);

    @Mappings({
            @Mapping(source = "offeringPropertyType", target = "offeringPropertyType"),
            @Mapping(target = "offering", ignore = true)
    })
    OfferingProperty to(OfferingPropertyDTO source);
    List<OfferingProperty> to(List<OfferingPropertyDTO> source);

    @Mappings({
            @Mapping(source = "offeringPropertyType", target = "offeringPropertyType"),
            @Mapping(source = "offering.offeringId", target = "offeringId"),
            @Mapping(source = "offering.offeringName", target = "offeringName"),
            @Mapping(target = "editMode", ignore = true),
    })
    OfferingPropertyDTO from(OfferingProperty source);
    List<OfferingPropertyDTO> from(List<OfferingProperty> source);


    default OfferingPropertyType ConvertOfferingPropertyType(int type) {
        return OfferingPropertyType.valueOf(type);
    }
    default int ConvertOfferingPropertyType(OfferingPropertyType type) { return type.getIndex(); }

}
