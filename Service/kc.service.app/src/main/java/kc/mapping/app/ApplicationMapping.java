package kc.mapping.app;

import kc.dto.app.AppSettingDTO;
import kc.dto.app.AppSettingPropertyDTO;
import kc.dto.app.AppTemplateDTO;
import kc.dto.app.ApplicationDTO;
import kc.enums.TemplateType;
import kc.framework.enums.AttributeDataType;
import kc.model.app.AppSetting;
import kc.model.app.AppSettingProperty;
import kc.model.app.DevTemplate;
import kc.model.app.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.EnumSet;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ApplicationMapping {
	// ApplicationMapping INSTANCE = Mappers.getMapper(ApplicationMapping.class);

	@Mappings({ })
	Application toApplication(ApplicationDTO source);
	List<Application> toApplicationList(List<ApplicationDTO> source);

	@Mappings({
			@Mapping(target = "isEditMode", ignore = true) })
	ApplicationDTO fromApplication(Application source);
	List<ApplicationDTO> fromApplicationDtoList(List<Application> source);

	@Mappings({ })
	DevTemplate toAppTemplate(AppTemplateDTO source);
	List<DevTemplate> toAppTemplateList(List<AppTemplateDTO> source);

	@Mappings({ @Mapping(target = "editMode", ignore = true) })
	AppTemplateDTO fromAppTemplate(DevTemplate source);
	List<AppTemplateDTO> fromAppTemplateList(List<DevTemplate> source);

	@Mappings({ @Mapping(target = "application", ignore = true) })
	AppSetting toAppSetting(AppSettingDTO source);
	List<AppSetting> toAppSettingList(List<AppSettingDTO> source);

	@Mappings({ @Mapping(source = "application.applicationId", target = "applicationId"),
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


	default TemplateType fromTemplateType(int type) {
		return TemplateType.valueOf(type);
	}
	default int toTemplateType(TemplateType type) {
		return type.getIndex();
	}

	default EnumSet<TemplateType> convertTemplateTypes(int type) {
		EnumSet<TemplateType> result = EnumSet.noneOf(TemplateType.class);
		if((type & TemplateType.BackEnd.getIndex()) > 0){
			result.add(TemplateType.BackEnd);
		}
		if((type & TemplateType.FrontEnd.getIndex()) > 0){
			result.add(TemplateType.FrontEnd);
		}
		if((type & TemplateType.Mobile.getIndex()) > 0){
			result.add(TemplateType.Mobile);
		}
		if((type & TemplateType.H5.getIndex()) > 0){
			result.add(TemplateType.H5);
		}

		return result;
	}
	default int convertTemplateTypes(EnumSet<TemplateType> types) {
		int v = 0;
		for (TemplateType version : types) {
			v = v | version.getIndex();
		}
		return v;
	}

	@Named("fromAttributeDataType")
	default AttributeDataType fromAttributeDataType(int status) {
		return AttributeDataType.valueOf(status);
	}
	@Named("toAttributeDataType")
	default int toAttributeDataType(AttributeDataType type) {
		return type.getIndex();
	}
}
