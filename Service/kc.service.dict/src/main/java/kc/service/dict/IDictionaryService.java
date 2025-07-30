package kc.service.dict;

import kc.dto.PaginatedBaseDTO;
import kc.dto.dict.*;

import java.util.List;

public interface IDictionaryService {
    List<ProvinceDTO> findAllProvinces();
    List<CityDTO> findCitiesByProvinceId(int provinceID);
    List<CityDTO> findAllCities();

    List<DictTypeDTO> findDictTypeList(String name);
    DictTypeDTO getDictTypeById(int id);
    boolean saveDictType(List<DictTypeDTO> models);
    boolean removeDictType(int id);

    List<DictValueDTO> findAllDictValuesByDictTypeCode(String dictTypeCode);
    PaginatedBaseDTO<DictValueDTO> findPaginatedDictValuesByFilter(
            int pageIndex, int pageSize, String name, Integer typeId);
    DictValueDTO getDictValueById(int id);
    boolean saveDictValue(List<DictValueDTO> data, Integer typeId, String operatorId, String operatorName);
    boolean removeDictValue(int id);

    List<IndustryClassficationDTO> findRootIndustryClassfications();
    List<IndustryClassficationDTO> findIndustryClassficationsByName(String name);
}
