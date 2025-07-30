package kc.mapping.app;

import kc.dto.app.ApplicationLogDTO;
import kc.enums.app.AppLogType;
import kc.framework.enums.ProcessLogType;
import kc.model.app.ApplicationLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ApplicationLogMapping {
    // ApplicationLogMapping INSTANCE = Mappers.getMapper(ApplicationLogMapping.class);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "appLogType", target = "appLogType")})
    ApplicationLog toApplicationLog(ApplicationLogDTO source);
    List<ApplicationLog> toApplicationLogList(List<ApplicationLogDTO> source);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "appLogType", target = "appLogType"),
            @Mapping(target = "typeString", ignore = true)})
    ApplicationLogDTO fromApplicationLog(ApplicationLog source);
    List<ApplicationLogDTO> fromApplicationLogList(List<ApplicationLog> source);

    default ProcessLogType toProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default int fromProcessLogType(ProcessLogType type) {
        return type.getIndex();
    }

    default AppLogType toAppLogType(int type) {
        return AppLogType.valueOf(type);
    }
    default int fromAppLogType(AppLogType type) {
        return type.getIndex();
    }
}
