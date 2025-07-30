package kc.service.config;

import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.config.ConfigAttributeDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.framework.enums.ConfigType;

public interface IConfigService {
	List<ConfigEntityDTO> findAll();
	PaginatedBaseDTO<ConfigEntityDTO> findPaginatedConfigsByNameAndType(int pageIndex, int pageSize, String configName, ConfigType configType);
	ConfigEntityDTO GetConfigById(int configId);
	boolean SaveConfig(ConfigEntityDTO model);
	boolean SoftRemoveConfigEntityById(int configId);
	
	List<ConfigAttributeDTO> GetConfigAttributesByConfigId(int configId);
    ConfigAttributeDTO GetPropertyById(int propertyId);
    boolean SaveConfigAttribute(ConfigAttributeDTO data);
    boolean SoftRemoveConfigAttributeById(int propertyId);
	
}
