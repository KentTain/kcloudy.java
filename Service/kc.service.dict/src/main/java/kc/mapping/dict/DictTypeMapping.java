package kc.mapping.dict;

import kc.dto.dict.DictValueDTO;
import kc.dto.dict.DictTypeDTO;
import kc.framework.enums.ConfigType;
import kc.model.dict.DictType;
import kc.model.dict.DictValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface DictTypeMapping {
	// DictTypeMapping INSTANCE = Mappers.getMapper(DictTypeMapping.class);

	@Mappings({
			@Mapping(source = "dictValues", target = "dictValues") })
	DictType toDictType(DictTypeDTO source);
	List<DictTypeDTO> toDictTypeDtoList(List<DictType> source);

	@Mappings({
			@Mapping(source = "dictValues", target = "dictValues") })
	DictTypeDTO fromDictType(DictType source);
	List<DictType> fromDictTypeList(List<DictTypeDTO> source);


	@Mappings({
			@Mapping(target = "dictType", ignore = true)
	})
	DictValue toDictValue(DictValueDTO source);
	List<DictValueDTO> toDictValueDtoList(List<DictValue> source);

	@Mappings({
			@Mapping(source = "dictType.id", target = "dictTypeId"),
			@Mapping(source = "dictTypeCode", target = "dictTypeCode"),
			@Mapping(source = "dictType.name", target = "dictTypeName"),
			@Mapping(target = "select", ignore = true)
	})
	DictValueDTO fromDictValue(DictValue source);
	List<DictValue> fromDictValueList(List<DictValueDTO> source);

	@Named("toConfigType")
	default ConfigType toConfigType(int configType) {
		return ConfigType.valueOf(configType);
	}
	@Named("fromConfigType")
	default Integer fromConfigType(ConfigType type) {
		if (null == type) return null;
		return type.getIndex();
	}

}
