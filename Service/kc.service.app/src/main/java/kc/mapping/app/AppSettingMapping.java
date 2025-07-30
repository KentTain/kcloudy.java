package kc.mapping.app;

import java.util.List;

import kc.dto.app.AppSettingDTO;
import kc.dto.app.AppSettingPropertyDTO;
import kc.model.app.AppSetting;
import kc.model.app.AppSettingProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.framework.enums.AttributeDataType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface AppSettingMapping {
	// AppSettingMapping INSTANCE = Mappers.getMapper(AppSettingMapping.class);

	@Mappings({ @Mapping(target = "application", ignore = true) })
	AppSetting toAppSetting(AppSettingDTO source);
	List<AppSetting> toAppSettingList(List<AppSettingDTO> source);

	@Mappings({
			@Mapping(target = "editMode", ignore = true),
			@Mapping(source = "application.applicationId", target = "applicationId"),
			@Mapping(source = "application.applicationName", target = "applicationName") })
	AppSettingDTO fromAppSetting(AppSetting source);
	List<AppSettingDTO> fromAppSettingList(List<AppSetting> source);


	@Mappings({ @Mapping(target = "appSetting", ignore = true),
		@Mapping(source = "dataType", target = "dataType", qualifiedByName = "toAttributeDataType") })
	AppSettingProperty toAppSettingProperty(AppSettingPropertyDTO source);
	List<AppSettingProperty> toAppSettingPropertyList(List<AppSettingPropertyDTO> source);

	@Mappings({ @Mapping(source = "appSetting.propertyId", target = "appSettingId"),
			@Mapping(source = "appSetting.name", target = "appSettingName"),
			@Mapping(source = "dataType", target = "dataType", qualifiedByName = "fromAttributeDataType") })
	AppSettingPropertyDTO fromAppSettingProperty(AppSettingProperty source);
	List<AppSettingPropertyDTO> fromAppSettingPropertyList(List<AppSettingProperty> source);


	@Named("fromAttributeDataType")
	default AttributeDataType fromAttributeDataType(int status) {
		return AttributeDataType.valueOf(status);
	}
	@Named("toAttributeDataType")
	default int toAttributeDataType(AttributeDataType type) {
		return type.getIndex();
	}
}
