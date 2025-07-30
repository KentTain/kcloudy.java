package kc.mapping.portal;

import kc.dto.portal.CompanyExtInfoDTO;
import kc.enums.portal.CompanyExtInfoType;
import kc.framework.enums.AttributeDataType;
import kc.model.portal.CompanyExtInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyExtInfoMapping {
    CompanyExtInfoMapping INSTANCE = Mappers.getMapper(CompanyExtInfoMapping.class);

    @Mappings({
            @Mapping(source = "dataType", target = "dataType", qualifiedByName = "fromAttributeDataType"),
            @Mapping(source = "companyExtInfoType", target = "companyExtInfoType"),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyExtInfo to(CompanyExtInfoDTO source);

    List<CompanyExtInfo> to(List<CompanyExtInfoDTO> source);

    @Mappings({

            @Mapping(source = "dataType", target = "dataType", qualifiedByName = "toAttributeDataType"),
            @Mapping(source = "companyExtInfoType", target = "companyExtInfoType"),
            @Mapping(target = "companyExtInfoTypeString", ignore = true),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyExtInfoDTO from(CompanyExtInfo source);

    List<CompanyExtInfoDTO> from(List<CompanyExtInfo> source);

    default CompanyExtInfoType ConvertCompanyExtInfoType(int type) {
        return CompanyExtInfoType.valueOf(type);
    }
    default int ConvertCompanyExtInfoType(CompanyExtInfoType type) { return type.getIndex(); }

    @Named("toAttributeDataType")
    default AttributeDataType toAttributeDataType(int status) {
        return AttributeDataType.valueOf(status);
    }
    @Named("fromAttributeDataType")
    default int fromAttributeDataType(AttributeDataType type) {
        return type.getIndex();
    }
}
