package kc.mapping.account;

import kc.dto.account.UserSettingDTO;
import kc.dto.account.UserSettingPropertyDTO;
import kc.framework.enums.AttributeDataType;
import kc.model.account.UserSetting;
import kc.model.account.UserSettingProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface UserSettingMapping {
    UserSettingMapping INSTANCE = Mappers.getMapper(UserSettingMapping.class);

    @Mappings({
    })
    UserSetting to(UserSettingDTO source);

    List<UserSetting> to(List<UserSettingDTO> source);

    @Mappings({

    })
    UserSettingDTO from(UserSetting source);

    List<UserSettingDTO> from(List<UserSetting> source);

    @Mappings({
            @Mapping(source = "dataType", target = "dataType", qualifiedByName = "toAttributeDataType")
    })
    UserSettingProperty toProperty(UserSettingPropertyDTO source);

    List<UserSettingProperty> toProperty(List<UserSettingPropertyDTO> source);

    @Mappings({
            @Mapping(source = "dataType", target = "dataType", qualifiedByName = "fromAttributeDataType")
    })
    UserSettingPropertyDTO fromProperty(UserSettingProperty source);

    List<UserSettingPropertyDTO> fromProperty(List<UserSettingProperty> source);

    @Named("fromAttributeDataType")
    default AttributeDataType fromAttributeDataType(int type) {
        return AttributeDataType.valueOf(type);
    }

    @Named("toAttributeDataType")
    default Integer toAttributeDataType(AttributeDataType type) {
        if (null == type) return null;
        return type.getIndex();
    }
}
