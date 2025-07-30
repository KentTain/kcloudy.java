package kc.mapping.portal;

import kc.dto.portal.CompanyAccountDTO;
import kc.enums.portal.BankAccountType;
import kc.model.portal.CompanyAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyAccountMapping {
    CompanyAccountMapping INSTANCE = Mappers.getMapper(CompanyAccountMapping.class);

    @Mappings({
            @Mapping(source = "bankType", target = "bankType"),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyAccount to(CompanyAccountDTO source);

    List<CompanyAccount> to(List<CompanyAccountDTO> source);

    @Mappings({
            @Mapping(source = "bankType", target = "bankType"),
            @Mapping(target = "bankTypeString", ignore = true),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyAccountDTO from(CompanyAccount source);

    List<CompanyAccountDTO> from(List<CompanyAccount> source);


    default BankAccountType ConvertBankType(int type) {
        return BankAccountType.valueOf(type);
    }
    default int ConvertBankType(BankAccountType type) { return type.getIndex(); }

}
