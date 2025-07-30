package kc.service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kc.dataaccess.config.IConfigAttributeRepository;
import kc.dataaccess.config.IConfigEntityRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.config.ConfigAttributeDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.framework.base.ConfigAttribute;
import kc.framework.base.ConfigEntity;
import kc.framework.enums.ConfigType;
import kc.framework.extension.StringExtensions;
import kc.mapping.config.ConfigEntityMapping;

@Service
public class ConfigService implements IConfigService {

	@Autowired
	private IConfigEntityRepository _configEntityRepository;
	@Autowired
	private IConfigAttributeRepository _configAttributeRepository;
	@Autowired
	private ConfigEntityMapping _configEntityMapping;

	@Override
	public List<ConfigEntityDTO> findAll() {
		List<ConfigEntity> data = _configEntityRepository.findAll();
		return _configEntityMapping.toConfigEntityDtoList(data);
	}
	
	@Override
	public PaginatedBaseDTO<ConfigEntityDTO> findPaginatedConfigsByNameAndType(int pageIndex, int pageSize,
			String configName, ConfigType configType) {

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<ConfigEntity> data = null;
		if (configType != null && !StringExtensions.isNullOrEmpty(configName)) {
			data = _configEntityRepository.findAllByConfigNameContainingAndConfigType(pageable, configName, configType);
		} else if (configType != null) {
			data = _configEntityRepository.findAllByConfigNameContaining(pageable, configName);
		} else {
			data = _configEntityRepository.findAll(pageable);
		}

		int total = data.getSize();
		List<ConfigEntityDTO> rows = _configEntityMapping.toConfigEntityDtoList(data.getContent());
		return new PaginatedBaseDTO<ConfigEntityDTO>(pageIndex, pageSize, total, rows);
	}

	@Override
	public ConfigEntityDTO GetConfigById(int configId) {
		ConfigEntity data = _configEntityRepository.findByConfigId(configId);
		return _configEntityMapping.fromConfigEntity(data);
	}

	@Override
	public boolean SaveConfig(ConfigEntityDTO model) {
		ConfigEntity data = _configEntityMapping.toConfigEntity(model);
		return _configEntityRepository.saveAndFlush(data) != null;
	}

	@Override
	public boolean SoftRemoveConfigEntityById(int configId) {
		ConfigEntity data = _configEntityRepository.findByConfigId(configId);
		data.setDeleted(true);
		return _configEntityRepository.saveAndFlush(data) != null;
	}

	@Override
	public List<ConfigAttributeDTO> GetConfigAttributesByConfigId(int configId) {
		List<ConfigAttribute> data = _configAttributeRepository.findByConfigId(configId);
		return _configEntityMapping.toConfigAttributeDtoList(data);
	}

	@Override
	public ConfigAttributeDTO GetPropertyById(int propertyId) {
		ConfigAttribute data = _configAttributeRepository.getOne(propertyId);
		return _configEntityMapping.fromConfigAttribute(data);
	}

	@Override
	public boolean SaveConfigAttribute(ConfigAttributeDTO data) {
		ConfigAttribute model = _configEntityMapping.toConfigAttribute(data);
		return _configAttributeRepository.saveAndFlush(model) != null;
	}

	@Override
	public boolean SoftRemoveConfigAttributeById(int propertyId) {
		ConfigAttribute data = _configAttributeRepository.getOne(propertyId);
		data.setDeleted(true);
		return _configAttributeRepository.saveAndFlush(data) != null;
	}
}
