package kc.webapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kc.dto.dict.*;
import kc.service.base.ServiceResult;
import kc.service.dict.IDictionaryService;
import kc.web.base.WebApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Api/Dictionary")
@Api(tags = "字典相关接口", description = "提供用户相关的 Rest API")
public class DictionaryApiController extends WebApiBaseController {
	@Autowired
	private IDictionaryService _dictService;

    @GetMapping("/LoadAllProvinces")
	public ServiceResult<List<ProvinceDTO>> LoadAllProvinces() {
		return GetServiceResult(() -> {
			return _dictService.findAllProvinces();
		}, log);
	}

	@GetMapping("/LoadAllCities")
	public ServiceResult<List<CityDTO>> LoadAllCities() {
		return GetServiceResult(() -> {
			return _dictService.findAllCities();
		}, log);
	}
    
    @GetMapping("/LoadCitiesByProvinceId/{provinceID}")
    public ServiceResult<List<CityDTO>> LoadCitiesByProvinceId(@PathVariable("provinceID") int provinceID) {
    	return GetServiceResult(() -> {
			return _dictService.findCitiesByProvinceId(provinceID);
		}, log);
    }

	@GetMapping("/LoadRootIndustryClassfications")
	public ServiceResult<List<IndustryClassficationDTO>> LoadRootIndustryClassfications() {
		return GetServiceResult(() -> {
			return _dictService.findRootIndustryClassfications();
		}, log);
	}


	@GetMapping("/LoadAllDictValuesByTypeCode/{code}")
	public ServiceResult<List<DictValueDTO>> LoadAllDictValuesByTypeCode(@PathVariable("code") String code) {
		return GetServiceResult(() -> {
			return _dictService.findAllDictValuesByDictTypeCode(code);
		}, log);
	}
}
