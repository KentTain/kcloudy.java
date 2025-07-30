package kc.service.dict;

import kc.dataaccess.dict.*;
import kc.dto.PaginatedBaseDTO;
import kc.dto.dict.*;
import kc.framework.extension.StringExtensions;
import kc.mapping.CycleAvoidingMappingContext;
import kc.mapping.dict.*;
import kc.model.dict.*;
import kc.service.webapiservice.IConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DictionaryService implements IDictionaryService {

	@Autowired
	private IProvinceRepository _provinceRepository;
	@Autowired
	private ICityRepository _cityRepository;
	@Autowired
	private ProvinceMapping _provinceMapping;

	@Autowired
	private IDictTypeRepository _dictTypeRepository;
	@Autowired
	private IDictValueRepository _dictValueRepository;
	@Autowired
	private DictTypeMapping _dictTypeMapping;

	@Autowired
	private IIndustryClassficationRepository _industryClassficationRepository;
	@Autowired
	private IndustryClassficationMapping _industryClassficationMapping;

	@Autowired
	private IConfigApiService _configApiService;

	@Override
	public List<ProvinceDTO> findAllProvinces() {
		List<Province> data = _provinceRepository.findAll();
		return _provinceMapping.toProvinceDtoList(data);
	}
	@Override
	public List<CityDTO> findCitiesByProvinceId(int provinceID) {
		List<City> data = _cityRepository.findByProvinceId(provinceID);
		return _provinceMapping.toCityDtoList(data);
	}
	@Override
	public List<CityDTO> findAllCities() {
		List<City> data = _cityRepository.findAll();
		return _provinceMapping.toCityDtoList(data);
	}

	@Override
	public List<DictTypeDTO> findDictTypeList(String name) {
		if(StringExtensions.isNullOrEmpty(name)) {
			List<DictType> data = _dictTypeRepository.findAll();
			return _dictTypeMapping.toDictTypeDtoList(data);
		}
		DictType dictType = new DictType();
		dictType.setName(name);
		//全部模糊查询，即%{name}%
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("name" ,ExampleMatcher.GenericPropertyMatchers.contains());
		Example<DictType> example = Example.of(dictType, matcher);

		List<DictType> data = _dictTypeRepository.findAll(example);
		return _dictTypeMapping.toDictTypeDtoList(data);
	}
	@Override
	public DictTypeDTO getDictTypeById(int id) {
		Optional<DictType> data = _dictTypeRepository.findById(id);
		if (!data.isPresent()) return null;

		DictType model = data.get();
		return _dictTypeMapping.fromDictType(model);
	}
	@Override
	public boolean saveDictType(List<DictTypeDTO> models) {
		List<DictType> data = _dictTypeMapping.fromDictTypeList(models);
		_dictTypeRepository.saveAll(data);

		_dictTypeRepository.flush();
		return true;
	}
	@Override
	public boolean removeDictType(int id) {
		Optional<DictType> data = _dictTypeRepository.findById(id);
		if (!data.isPresent()) return true;

		DictType model = data.get();
		model.setDeleted(true);
		return _dictTypeRepository.saveAndFlush(model) != null;
	}

	@Override
	public List<DictValueDTO> findAllDictValuesByDictTypeCode(String dictTypeCode) {
		if(StringExtensions.isNullOrEmpty(dictTypeCode)) {
			List<DictValue> data = _dictValueRepository.findAll();
			return _dictTypeMapping.toDictValueDtoList(data);
		}

		DictValue dictType = new DictValue();
		dictType.setDictTypeCode(dictTypeCode);
		Example<DictValue> example = Example.of(dictType);

		List<DictValue> data = _dictValueRepository.findAll(example);
		return _dictTypeMapping.toDictValueDtoList(data);
	}
	@Override
	public PaginatedBaseDTO<DictValueDTO> findPaginatedDictValuesByFilter(
			int pageIndex, int pageSize, String name, Integer typeId) {
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<DictValue> data = null;
		if (typeId != null && typeId != 0 && !StringExtensions.isNullOrEmpty(name)) {
			data = _dictValueRepository.findAllByNameContainingAndDictTypeId(pageable, name, typeId);
		} else if ((typeId == null || typeId == 0) && !StringExtensions.isNullOrEmpty(name)){
			data = _dictValueRepository.findAllByNameContaining(pageable, name);
		} else {
			data = _dictValueRepository.findAll(pageable);
		}

		int total = data.getSize();
		List<DictValueDTO> rows = _dictTypeMapping.toDictValueDtoList(data.getContent());
		return new PaginatedBaseDTO<DictValueDTO>(pageIndex, pageSize, total, rows);
	}
	@Override
	public DictValueDTO getDictValueById(int id) {
		Optional<DictValue> data = _dictValueRepository.findById(id);
		if (!data.isPresent()) return null;

		DictValue model = data.get();
		return _dictTypeMapping.fromDictValue(model);
	}
	@Override
	public boolean saveDictValue(List<DictValueDTO> models, Integer typeId, String operatorId, String operatorName) {
		DictType type = _dictTypeRepository.findDetailById(typeId);
		if (type == null)
			throw new IllegalArgumentException(String.format("未找到ID【%s】相关的类型", typeId));

		List<DictValue> data = _dictTypeMapping.fromDictValueList(models);
		for (DictValue model : data) {
			model.setDictType(type);
			model.setDictTypeCode(type.getCode());
			if (model.getId() == 0){
				String code = _configApiService.GetSeedCodeByName("DictionaryValue");
				model.setCode(code);
				model.setCreatedBy(operatorId);
				model.setCreatedName(operatorName);
				model.setCreatedDate(new Date());
				model.setModifiedBy(operatorId);
				model.setModifiedName(operatorName);
				model.setModifiedDate(new Date());
			} else {
				model.setModifiedBy(operatorId);
				model.setModifiedName(operatorName);
				model.setModifiedDate(new Date());
			}

			_dictValueRepository.save(model);
		}
		_dictValueRepository.flush();
		return true;
	}

	@Override
	public boolean removeDictValue(int id) {
		Optional<DictValue> data = _dictValueRepository.findById(id);
		if (!data.isPresent()) return true;

		DictValue model = data.get();
		model.setDeleted(true);
		return _dictValueRepository.saveAndFlush(model) != null;
	}

	@Override
	public List<IndustryClassficationDTO> findRootIndustryClassfications()
	{
		List<IndustryClassfication> data = _industryClassficationRepository.findAllTreeNodesWithNestParentAndChildByName(IndustryClassfication.class, null);
		return _industryClassficationMapping.from(data, new CycleAvoidingMappingContext());
	}
	@Override
	public List<IndustryClassficationDTO> findIndustryClassficationsByName(String name)
	{
		List<IndustryClassfication> data = _industryClassficationRepository.findAllTreeNodesWithNestParentAndChildByName(IndustryClassfication.class, name);
		return _industryClassficationMapping.from(data, new CycleAvoidingMappingContext());
	}
}
