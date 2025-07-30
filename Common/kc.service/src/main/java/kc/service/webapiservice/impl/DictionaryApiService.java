package kc.service.webapiservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import kc.dto.dict.CityDTO;
import kc.dto.dict.DictValueDTO;
import kc.dto.dict.IndustryClassficationDTO;
import kc.dto.dict.ProvinceDTO;
import kc.framework.tenant.ApplicationConstant;
import kc.service.webapiservice.IDictionaryApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryApiService extends IdSrvWebApiServiceBase implements IDictionaryApiService {
	private final static String ServiceName = "kc.service.webapiservice.impl.DictionaryApiService";

	@Override
	public List<ProvinceDTO> LoadAllProvinces() {
		List<ProvinceDTO> result = new ArrayList<>();
		result = WebSendGet(
			new TypeReference<List<ProvinceDTO>>() {},
            ServiceName + ".LoadAllProvinces",
				DictionaryApiUrl() + "DictionaryApi/LoadAllProvinces",
            ApplicationConstant.DicScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

         return result;
	}

	@Override
	public List<CityDTO> LoadAllCities() {
		List<CityDTO> result = new ArrayList<>();
		result = WebSendGet(
				new TypeReference<List<CityDTO>>() {},
				ServiceName + ".LoadAllCities",
				DictionaryApiUrl() + "DictionaryApi/LoadAllCities",
				ApplicationConstant.DicScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					failCallback.getErrMsg();
				},
				true);

		return result;
	}

	@Override
	public List<CityDTO> LoadCitiesByProvinceId(int provinceID) {
		List<CityDTO> result = new ArrayList<>();
		result = WebSendGet(
				new TypeReference<List<CityDTO>>() {},
				ServiceName + ".LoadCitiesByProvinceId",
				DictionaryApiUrl() + "DictionaryApi/LoadCitiesByProvinceId?provinceID=" + provinceID,
				ApplicationConstant.DicScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					failCallback.getErrMsg();
				},
				true);

		return result;
	}

	@Override
	public List<IndustryClassficationDTO> LoadRootIndustryClassfications() {
		List<IndustryClassficationDTO> result = new ArrayList<>();
		result = WebSendGet(
				new TypeReference<List<IndustryClassficationDTO>>() {},
				ServiceName + ".LoadRootIndustryClassfications",
				DictionaryApiUrl() + "DictionaryApi/LoadRootIndustryClassfications",
				ApplicationConstant.DicScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					failCallback.getErrMsg();
				},
				true);

		return result;
	}

	@Override
	public List<DictValueDTO> LoadAllDictValuesByTypeCode(String code) {
		List<DictValueDTO> result = new ArrayList<>();
		result = WebSendGet(
				new TypeReference<List<DictValueDTO>>() {},
				ServiceName + ".LoadCitiesByProvinceId",
				DictionaryApiUrl() + "DictionaryApi/LoadAllDictValuesByTypeCode?code=" + code,
				ApplicationConstant.DicScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					failCallback.getErrMsg();
				},
				true);

		return result;
	}
}
