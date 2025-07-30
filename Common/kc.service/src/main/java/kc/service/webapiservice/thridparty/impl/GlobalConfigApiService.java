package kc.service.webapiservice.thridparty.impl;

import kc.framework.extension.StringExtensions;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.service.WebApiServiceBase;
import kc.service.constants.OpenIdConnectConstants;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import kc.framework.GlobalConfigData;

@Service
@lombok.extern.slf4j.Slf4j
public class GlobalConfigApiService  extends WebApiServiceBase implements IGlobalConfigApiService{
	private final static String ServiceName = "kc.service.webapi.GlobalConfigApiService";
	
	public GlobalConfigApiService() {
	}
	
	@Override
	protected OAuth2ClientInfo getOAuth2ClientInfo() {
		return super.getTenantOAuth2ClientInfo(TenantConstant.DbaTenantApiAccessInfo);
	}
	
	@Override
	protected String SSOServerUrl() {
		Tenant tenant =  TenantConstant.DbaTenantApiAccessInfo;
		String tenantName = tenant.getTenantName();

		if (StringExtensions.isNullOrEmpty(super.ssoWebDomain))
			return null;

		if (super.ssoWebDomain.startsWith("http://"))
			return super.ssoWebDomain.replace("http://", "http://" + tenantName + ".");

		return super.ssoWebDomain.replace("https://", "https://" + tenantName + ".");
	}
	
	/* 获取全局配置
	 * @see kc.service.webapi.IGlobalConfigApiService#GetGlobalConfigData()
	 */
	@Override
	public GlobalConfigData GetGlobalConfigData() {
		GlobalConfigData result = null;
		result = WebSendGet(
			new TypeReference<GlobalConfigData>() {},
			ServiceName + ".GetGlobalConfigData",
				super.resWebDomain + "api/GlobalConfigApi/GetData",
            ApplicationConstant.SsoScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	log.error(ServiceName + " throw error: " + failCallback.toString());
            },
            false);

         return result;
	}

	/* 获取全局配置
	 * @see kc.service.webapi.IGlobalConfigApiService#GetGlobalConfigData()
	 */
	@Override
	public String GetUEditorConfig() {
		String result = null;
		result = WebSendGet(
				new TypeReference<String>() {},
				ServiceName + ".GetUEditorConfig",
				super.resWebDomain + "js/ueditor/config.json",
				ApplicationConstant.SsoScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					log.error(ServiceName + " throw error: " + failCallback.toString());
				},
				false);

		return result;
	}
	
}
