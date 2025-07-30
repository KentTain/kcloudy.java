package kc.mapping.portal;

import kc.dto.BlobInfoDTO;
import kc.dto.portal.OfferingDTO;
import kc.dto.portal.OfferingSimpleDTO;
import kc.enums.portal.*;
import kc.framework.util.SerializeHelper;
import kc.model.portal.Offering;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OfferingPropertyMapping.class, OfferingPropertyMapping.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface OfferingMapping {
    OfferingMapping INSTANCE = Mappers.getMapper(OfferingMapping.class);

    @Mappings({
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "offeringImageBlob", target = "offeringImageBlob"),
            @Mapping(source = "offeringFileBlob", target = "offeringFileBlob"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "offeringOperationLogs", ignore = true)
    })
    Offering to(OfferingDTO source);
    List<Offering> to(List<OfferingDTO> source);

    @Mappings({
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "offeringImageBlob", target = "offeringImageBlob"),
            @Mapping(source = "offeringFileBlob", target = "offeringFileBlob"),
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
            @Mapping(source = "offeringImageBlob", target = "offeringImageBlob"),
            @Mapping(source = "offeringFileBlob", target = "offeringFileBlob"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "showImageUrl", ignore = true),
            @Mapping(target = "downloadFileUrl", ignore = true),
            @Mapping(target = "offeringImageBlobs", ignore = true),
            @Mapping(target = "offeringTypeString", ignore = true),
            @Mapping(target = "statusString", ignore = true)
    })
    OfferingDTO from(Offering source);
    List<OfferingDTO> from(List<Offering> source);

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "category.name", target = "categoryName"),
            @Mapping(source = "offeringType", target = "offeringType"),
            @Mapping(source = "offeringImageBlob", target = "offeringImageBlob"),
            @Mapping(source = "offeringFileBlob", target = "offeringFileBlob"),
            @Mapping(source = "status", target = "status"),
            @Mapping(target = "editMode", ignore = true),
            @Mapping(target = "showImageUrl", ignore = true),
            @Mapping(target = "downloadFileUrl", ignore = true),
            @Mapping(target = "offeringTypeString", ignore = true),
            @Mapping(target = "statusString", ignore = true)
    })
    OfferingSimpleDTO fromSimple(Offering source);
    List<OfferingSimpleDTO> fromSimple(List<Offering> source);

    default BlobInfoDTO ConvertBlob(String blobJson){
        return SerializeHelper.FromJson(blobJson, BlobInfoDTO.class);
    }
    default String ConvertBlob(BlobInfoDTO blob){
        return SerializeHelper.ToJson(blob);
    }

    default OfferingType ConvertOfferingType(int type) {
        return OfferingType.valueOf(type);
    }
    default int ConvertOfferingType(OfferingType type) { return type.getIndex(); }

    default OfferingStatus ConvertOfferingStatus(int type) {
        return OfferingStatus.valueOf(type);
    }
    default int ConvertOfferingStatus(OfferingStatus type) { return type.getIndex(); }

}
