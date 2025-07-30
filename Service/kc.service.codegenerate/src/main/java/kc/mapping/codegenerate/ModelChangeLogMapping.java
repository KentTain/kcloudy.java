package kc.mapping.codegenerate;

import kc.dto.codegenerate.ModelChangeLogDTO;
import kc.enums.codegenerate.ModelType;
import kc.framework.enums.ProcessLogType;
import kc.model.codegenerate.ModelChangeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ModelChangeLogMapping {
    // ModelChangeLogMapping INSTANCE = Mappers.getMapper(ModelChangeLogMapping.class);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "modelType", target = "modelType")})
    ModelChangeLog toModelChangeLog(ModelChangeLogDTO source);
    List<ModelChangeLog> toModelChangeLogList(List<ModelChangeLogDTO> source);

    @Mappings({
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "modelType", target = "modelType"),
            @Mapping(target = "typeString", ignore = true),
            @Mapping(target = "modelTypeString", ignore = true)})
    ModelChangeLogDTO fromModelChangeLog(ModelChangeLog source);
    List<ModelChangeLogDTO> fromModelChangeLogList(List<ModelChangeLog> source);

    default ProcessLogType toProcessLogType(int type) {
        return ProcessLogType.valueOf(type);
    }
    default Integer fromProcessLogType(ProcessLogType type) {
        if (null == type) return null;
        return type.getIndex();
    }

    default ModelType toModelType(int type) {
        return ModelType.valueOf(type);
    }
    default Integer fromModelType(ModelType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
