package kc.mapping.dict;

import kc.dto.dict.ProvinceDTO;
import kc.dto.dict.CityDTO;
import kc.framework.enums.ConfigType;
import kc.model.dict.Province;
import kc.model.dict.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ProvinceMapping {
	// ProvinceMapping INSTANCE = Mappers.getMapper(ProvinceMapping.class);

	@Mappings({
			@Mapping(source = "cities", target = "cities") })
	Province toProvince(ProvinceDTO source);
	@Mappings({
			@Mapping(source = "cities", target = "cities") })
	ProvinceDTO fromProvince(Province source);

	List<ProvinceDTO> toProvinceDtoList(List<Province> source);


	@Mappings({
			@Mapping(target = "province", ignore = true)
	})
	City toCity(CityDTO source);
	@Mappings({
			@Mapping(source = "province.provinceId", target = "provinceId"),
			@Mapping(source = "province.name", target = "provinceName")
	})
	CityDTO fromCity(City source);
	
	List<CityDTO> toCityDtoList(List<City> source);

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
