package kc.mapping.account;

import kc.dto.account.UserLoginLogDTO;
import kc.framework.enums.ProcessLogType;
import kc.model.account.UserLoginLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface UserLoginLogMapping {
    UserLoginLogMapping INSTANCE = Mappers.getMapper(UserLoginLogMapping.class);

    @Mappings({
            //@Mapping(target = "user", ignore = true),
    })
    UserLoginLog to(UserLoginLogDTO source);
    List<UserLoginLog> to(List<UserLoginLogDTO> source);

    @Mappings({
            @Mapping(target = "typeString", ignore = true),
    })
    UserLoginLogDTO from(UserLoginLog source);
    List<UserLoginLogDTO> from(List<UserLoginLog> source);

    default ProcessLogType fromProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default Integer toProcessLogType(ProcessLogType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
