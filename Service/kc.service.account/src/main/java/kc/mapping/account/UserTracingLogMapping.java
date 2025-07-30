package kc.mapping.account;

import kc.dto.account.UserTracingLogDTO;
import kc.framework.enums.ProcessLogType;
import kc.model.account.UserTracingLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface UserTracingLogMapping {
    UserTracingLogMapping INSTANCE = Mappers.getMapper(UserTracingLogMapping.class);

    @Mappings({
    })
    UserTracingLog to(UserTracingLogDTO source);

    List<UserTracingLog> to(List<UserTracingLogDTO> source);

    @Mappings({
            @Mapping(target = "typeString", ignore = true),
    })
    UserTracingLogDTO from(UserTracingLog source);

    List<UserTracingLogDTO> from(List<UserTracingLog> source);


    default ProcessLogType fromProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }

    default Integer toProcessLogType(ProcessLogType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
