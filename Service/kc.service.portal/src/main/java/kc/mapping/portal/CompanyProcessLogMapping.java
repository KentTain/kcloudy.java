package kc.mapping.portal;

import kc.dto.portal.CompanyProcessLogDTO;
import kc.framework.enums.BusinessType;
import kc.framework.enums.ProcessLogType;
import kc.model.portal.CompanyProcessLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CompanyProcessLogMapping {
    CompanyProcessLogMapping INSTANCE = Mappers.getMapper(CompanyProcessLogMapping.class);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(target = "companyInfo", ignore = true),
    })
    CompanyProcessLog to(CompanyProcessLogDTO source);
    List<CompanyProcessLog> to(List<CompanyProcessLogDTO> source);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "companyInfo.companyId", target = "companyId"),
    })
    CompanyProcessLogDTO from(CompanyProcessLog source);
    List<CompanyProcessLogDTO> from(List<CompanyProcessLog> source);

    default ProcessLogType fromProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default int toProcessLogType(ProcessLogType type) {
        return type.getIndex();
    }
}
