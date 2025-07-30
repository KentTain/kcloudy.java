package kc.mapping.config;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.framework.base.ConfigAttribute;
import kc.framework.base.ConfigEntity;
import kc.framework.enums.AttributeDataType;
import kc.framework.enums.ConfigStatus;
import kc.framework.enums.ConfigType;
import kc.dto.config.ConfigAttributeDTO;
import kc.dto.config.ConfigEntityDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ConfigEntityMapping {
	// ConfigEntityMapping INSTANCE = Mappers.getMapper(ConfigEntityMapping.class);

	@Mappings({ @Mapping(source = "configType", target = "configType", qualifiedByName = "fromConfigType"),
			@Mapping(source = "state", target = "state", qualifiedByName = "fromConfigStatus") })
	ConfigEntity toConfigEntity(ConfigEntityDTO source);
	@Mappings({ @Mapping(source = "configType", target = "configType", qualifiedByName = "toConfigType"),
			@Mapping(source = "state", target = "state", qualifiedByName = "toConfigStatus") })
	ConfigEntityDTO fromConfigEntity(ConfigEntity source);

	List<ConfigEntityDTO> toConfigEntityDtoList(List<ConfigEntity> source);

	
	@Mappings({ @Mapping(target = "configEntity", ignore = true),
		@Mapping(source = "dataType", target = "dataType", qualifiedByName = "fromAttributeDataType") })
	ConfigAttribute toConfigAttribute(ConfigAttributeDTO source);
	@Mappings({ @Mapping(target = "configEntity", ignore = true),
		@Mapping(source = "dataType", target = "dataType", qualifiedByName = "toAttributeDataType") })
	ConfigAttributeDTO fromConfigAttribute(ConfigAttribute source);
	
	List<ConfigAttributeDTO> toConfigAttributeDtoList(List<ConfigAttribute> source);

	@Named("toConfigType")
	default ConfigType toConfigType(int configType) {
		return ConfigType.valueOf(configType);
	}
	@Named("fromConfigType")
	default Integer fromConfigType(ConfigType type) {
		if (null == type) return null;
		return type.getIndex();
	}

	@Named("toConfigStatus")
	default ConfigStatus toConfigStatus(int status) {
		return ConfigStatus.valueOf(status);
	}
	@Named("fromConfigStatus")
	default Integer fromConfigStatus(ConfigStatus type) {
		if (null == type) return null;
		return type.getIndex();
	}
	
	@Named("toAttributeDataType")
	default AttributeDataType toAttributeDataType(int status) {
		return AttributeDataType.valueOf(status);
	}
	@Named("fromAttributeDataType")
	default Integer fromAttributeDataType(AttributeDataType type) {
		if (null == type) return null;
		return type.getIndex();
	}
}
