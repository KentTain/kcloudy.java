package kc.service.webapiservice;

import kc.dto.dict.CityDTO;
import kc.dto.dict.DictValueDTO;
import kc.dto.dict.IndustryClassficationDTO;
import kc.dto.dict.ProvinceDTO;

import java.util.List;

public interface IDictionaryApiService {
	List<ProvinceDTO> LoadAllProvinces();
	List<CityDTO> LoadAllCities();
	List<CityDTO> LoadCitiesByProvinceId(int provinceID);

	List<IndustryClassficationDTO> LoadRootIndustryClassfications();

	List<DictValueDTO> LoadAllDictValuesByTypeCode(String code);
}
