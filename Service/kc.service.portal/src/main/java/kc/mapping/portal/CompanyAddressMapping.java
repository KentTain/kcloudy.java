package kc.mapping.portal;

import kc.dto.portal.CompanyAddressDTO;
import kc.enums.portal.AddressType;
import kc.model.portal.CompanyAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyAddressMapping {
    CompanyAddressMapping INSTANCE = Mappers.getMapper(CompanyAddressMapping.class);

    @Mappings({
            @Mapping(source = "addressType", target = "addressType"),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyAddress to(CompanyAddressDTO source);

    List<CompanyAddress> to(List<CompanyAddressDTO> source);

    @Mappings({
            @Mapping(source = "addressType", target = "addressType"),
            @Mapping(target = "addressTypeString", ignore = true),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyAddressDTO from(CompanyAddress source);

    List<CompanyAddressDTO> from(List<CompanyAddress> source);


    default AddressType ConvertAddressType(int type) {
        return AddressType.valueOf(type);
    }
    default int ConvertAddressType(AddressType type) { return type.getIndex(); }

}
