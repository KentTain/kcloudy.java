package kc.mapping.portal;

import kc.dto.BlobInfoDTO;
import kc.dto.portal.CompanyInfoDTO;
import kc.dto.portal.CompanyInfoSimpleDTO;
import kc.enums.portal.BusinessModel;
import kc.framework.util.SerializeHelper;
import kc.model.portal.CompanyInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CompanyAccountMapping.class, CompanyAddressMapping.class, CompanyContactMapping.class, CompanyExtInfoMapping.class, CompanyProcessLogMapping.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyInfoMapping {
    CompanyInfoMapping INSTANCE = Mappers.getMapper(CompanyInfoMapping.class);

    @Mappings({
            @Mapping(source = "businessModel", target = "businessModel"),
    })
    CompanyInfo to(CompanyInfoDTO source);
    List<CompanyInfo> to(List<CompanyInfoDTO> source);

    @Mappings({
            @Mapping(source = "businessModel", target = "businessModel"),
            @Mapping(target = "companyContacts", ignore = true),
            @Mapping(target = "companyAddresses", ignore = true),
            @Mapping(target = "companyAccounts", ignore = true),
            @Mapping(target = "companyExtInfos", ignore = true),
            @Mapping(target = "companyProcessLogs", ignore = true),
    })
    CompanyInfo toSimple(CompanyInfoSimpleDTO source);
    List<CompanyInfo> toSimple(List<CompanyInfoSimpleDTO> source);

    @Mappings({
            @Mapping(source = "businessModel", target = "businessModel"),
            @Mapping(target = "businessModelString", ignore = true),
    })
    CompanyInfoDTO from(CompanyInfo source);
    List<CompanyInfoDTO> from(List<CompanyInfo> source);

    @Mappings({
            @Mapping(source = "businessModel", target = "businessModel"),
            @Mapping(target = "businessModelString", ignore = true),
    })
    CompanyInfoSimpleDTO fromSimple(CompanyInfo source);
    List<CompanyInfoSimpleDTO> fromSimple(List<CompanyInfo> source);


    default BusinessModel ConvertBusinessModel(int type) {
        return BusinessModel.valueOf(type);
    }
    default int ConvertBusinessModel(BusinessModel type) { return type.getIndex(); }

    default BlobInfoDTO ConvertBlob(String blobJson){
        return SerializeHelper.FromJson(blobJson, BlobInfoDTO.class);
    }
    default String ConvertBlob(BlobInfoDTO blob){
        return SerializeHelper.ToJson(blob);
    }

}
