package kc.mapping.offering;

import kc.model.offering.*;
import kc.dto.offering.*;
import kc.enums.offering.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.EnumSet;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OfferingPropertyMapping.class, OfferingPropertyMapping.class, ProductMapping.class, ProductPropertyMapping.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface OfferingMapping {
    OfferingMapping INSTANCE = Mappers.getMapper(OfferingMapping.class);

    @Mappings({
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "offeringOperationLogs", ignore = true)
    })
    Offering to(OfferingDTO source);
    List<Offering> to(List<OfferingDTO> source);

    @Mappings({
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "offeringOperationLogs", ignore = true)
    })
    Offering toSimple(OfferingSimpleDTO source);
    List<Offering> toSimple(List<OfferingSimpleDTO> source);

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "category.name", target = "categoryName"),
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "multiSpecification", ignore = true),
            @Mapping(target = "downloadFileUrl", ignore = true),
            @Mapping(target = "offeringTypeString", ignore = true),
            @Mapping(target = "statusString", ignore = true)
    })
    OfferingDTO from(Offering source);
    List<OfferingDTO> from(List<Offering> source);

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "category.name", target = "categoryName"),
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "showImageUrl", ignore = true),
            @Mapping(target = "downloadFileUrl", ignore = true),
            @Mapping(target = "offeringTypeString", ignore = true),
            @Mapping(target = "statusString", ignore = true)
    })
    OfferingSimpleDTO fromSimple(Offering source);
    List<OfferingSimpleDTO> fromSimple(List<Offering> source);


    default OfferingType ConvertOfferingType(int type) {
        return OfferingType.valueOf(type);
    }
    default int ConvertOfferingType(OfferingType type) { return type.getIndex(); }

    default OfferingStatus ConvertOfferingStatus(int type) {
        return OfferingStatus.valueOf(type);
    }
    default int ConvertOfferingStatus(OfferingStatus type) { return type.getIndex(); }

    default ServiceDataType ConvertServiceDataType(int type) {
        return ServiceDataType.valueOf(type);
    }
    default int ConvertServiceDataType(ServiceDataType type) { return type.getIndex(); }

    default ServiceAttrDataType ConvertServiceAttrDataType(int type) {
        return ServiceAttrDataType.valueOf(type);
    }
    default int ConvertServiceAttrDataType(ServiceAttrDataType type) { return type.getIndex(); }

    default OfferingVersion ConvertOfferingVersion(int type) { return OfferingVersion.valueOf(type); }
    default int ConvertOfferingVersion(OfferingVersion type) { return type.getIndex(); }

    default EnumSet<OfferingVersion> ConvertOfferingVersions(int type) {
        EnumSet<OfferingVersion> result = EnumSet.noneOf(OfferingVersion.class);
        if((type & OfferingVersion.Free.getIndex()) > 0){
            result.add(OfferingVersion.Free);
        }
        if((type & OfferingVersion.TryOut.getIndex()) > 0){
            result.add(OfferingVersion.TryOut);
        }
        if((type & OfferingVersion.Normal.getIndex()) > 0){
            result.add(OfferingVersion.Normal);
        }
        if((type & OfferingVersion.Enterprise.getIndex()) > 0){
            result.add(OfferingVersion.Enterprise);
        }
        if((type & OfferingVersion.Group.getIndex()) > 0){
            result.add(OfferingVersion.Group);
        }

        return result;
    }
    default int ConvertOfferingVersions(EnumSet<OfferingVersion> types) {
        int v = 0;
        for (OfferingVersion version : types) {
            v = v | version.getIndex();
        }
        return v;
    }


}
