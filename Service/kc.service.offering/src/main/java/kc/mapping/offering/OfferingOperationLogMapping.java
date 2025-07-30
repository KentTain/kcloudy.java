package kc.mapping.offering;

import kc.framework.enums.ProcessLogType;
import kc.model.offering.*;
import kc.dto.offering.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface OfferingOperationLogMapping {
    OfferingOperationLogMapping INSTANCE = Mappers.getMapper(OfferingOperationLogMapping.class);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(target = "offering", ignore = true)
    })
    OfferingOperationLog to(OfferingOperationLogDTO source);

    List<OfferingOperationLog> to(List<OfferingOperationLogDTO> source);

    @Mappings({
            @Mapping(source = "type", target = "typeString"),
            @Mapping(source = "type", target = "type"),
    })
    OfferingOperationLogDTO from(OfferingOperationLog source);

    List<OfferingOperationLogDTO> from(List<OfferingOperationLog> source);

    default ProcessLogType ConvertProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default int ConvertProcessLogType(ProcessLogType type) { return type.getIndex(); }

    default String ConvertProcessLogTypeString(int type) {
        return ProcessLogType.getDesc(type);
    }
}
