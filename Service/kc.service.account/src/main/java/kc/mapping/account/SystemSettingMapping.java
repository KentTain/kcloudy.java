package kc.mapping.account;

import kc.dto.account.SystemSettingDTO;
import kc.dto.account.SystemSettingPropertyDTO;
import kc.framework.enums.AttributeDataType;
import kc.model.account.SystemSetting;
import kc.model.account.SystemSettingProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface SystemSettingMapping {
    SystemSettingMapping INSTANCE = Mappers.getMapper(SystemSettingMapping.class);

    @Mappings({
    })
    SystemSetting to(SystemSettingDTO source);

    List<SystemSetting> to(List<SystemSettingDTO> source);

    @Mappings({

    })
    SystemSettingDTO from(SystemSetting source);

    List<SystemSettingDTO> from(List<SystemSetting> source);

    @Mappings({
            @Mapping(source = "dataType", target = "dataType"),
            @Mapping(target = "systemSetting", ignore = true),
    })
    SystemSettingProperty toProperty(SystemSettingPropertyDTO source);

    List<SystemSettingProperty> toProperties(List<SystemSettingPropertyDTO> source);

    @Mappings({
            @Mapping(source = "dataType", target = "dataType"),
            @Mapping(target = "systemSetting", ignore = true),
    })
    SystemSettingPropertyDTO fromProperty(SystemSettingProperty source);

    List<SystemSettingPropertyDTO> fromProperties(List<SystemSettingProperty> source);

    default AttributeDataType fromAttributeDataType(int type) {
        return AttributeDataType.valueOf(type);
    }

    default Integer toAttributeDataType(AttributeDataType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
