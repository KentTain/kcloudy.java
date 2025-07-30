package kc.mapping.offering;

import kc.dto.offering.CategoryOperationLogDTO;
import kc.framework.enums.ProcessLogType;
import kc.model.offering.CategoryOperationLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CategoryOperationLogMapping {
    CategoryOperationLogMapping INSTANCE = Mappers.getMapper(CategoryOperationLogMapping.class);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(target = "category", ignore = true)
    })
    CategoryOperationLog to(CategoryOperationLogDTO source);

    List<CategoryOperationLog> to(List<CategoryOperationLogDTO> source);

    @Mappings({
            @Mapping(source = "type", target = "typeString"),
            @Mapping(source = "type", target = "type"),
    })
    CategoryOperationLogDTO from(CategoryOperationLog source);

    List<CategoryOperationLogDTO> from(List<CategoryOperationLog> source);

    default ProcessLogType ConvertProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default int ConvertProcessLogType(ProcessLogType type) { return type.getIndex(); }

    default String ConvertProcessLogTypeString(int type) {
        return ProcessLogType.getDesc(type);
    }
}
