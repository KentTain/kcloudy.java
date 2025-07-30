package kc.service.webapiservice.thridparty;

import kc.framework.GlobalConfigData;

public interface IGlobalConfigApiService {

	//@Override
	GlobalConfigData GetGlobalConfigData();

	/* 获取全局配置
	 * @see kc.service.webapi.IGlobalConfigApiService#GetGlobalConfigData()
	 */
	String GetUEditorConfig();
}