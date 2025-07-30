package kc.mapping.portal;

import kc.dto.portal.CompanyContactDTO;
import kc.framework.enums.BusinessType;
import kc.model.portal.CompanyContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyContactMapping {
    CompanyContactMapping INSTANCE = Mappers.getMapper(CompanyContactMapping.class);

    @Mappings({
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyContact to(CompanyContactDTO source);

    List<CompanyContact> to(List<CompanyContactDTO> source);

    @Mappings({
            @Mapping(source = "businessType", target = "businessType"),
            @Mapping(target = "businessTypeString", ignore = true),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyContactDTO from(CompanyContact source);

    List<CompanyContactDTO> from(List<CompanyContact> source);


    default BusinessType ConvertBusinessType(int type) {
        return BusinessType.valueOf(type);
    }
    default int ConvertBusinessType(BusinessType type) { return type.getIndex(); }

}
