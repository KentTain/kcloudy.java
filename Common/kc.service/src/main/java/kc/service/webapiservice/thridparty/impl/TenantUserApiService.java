package kc.service.webapiservice.thridparty.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.service.WebApiServiceBase;
import kc.service.base.ServiceResult;
import kc.service.constants.OpenIdConnectConstants;
import kc.service.webapiservice.thridparty.ITenantUserApiService;

@Service
@lombok.extern.slf4j.Slf4j
public class TenantUserApiService extends WebApiServiceBase implements ITenantUserApiService {
	private final static String ServiceName = "kc.service.webapi.TenantUserApiService";

	public TenantUserApiService() {
		
	}

	@Override
	protected OAuth2ClientInfo getOAuth2ClientInfo() {
		return super.getTenantOAuth2ClientInfo(TenantConstant.DbaTenantApiAccessInfo);
	}

	@Override
	public ServiceResult<Boolean> ExistTenantName(String tenantName) {
		ServiceResult<Boolean> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<Boolean>>() {}, 
				ServiceName + ".ExistTenantName",
				AdminApiServiceUrl() + "TenantApi/ExistTenantName?tenantName=" + tenantName,
				ApplicationConstant.AdminScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, false);

		return result;
	}

	@Override
	public Tenant GetTenantByName(String tenantName) {
		ServiceResult<Tenant> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<Tenant>>() {}, 
				ServiceName + ".GetTenantUserByName",
				AdminApiServiceUrl() + "TenantApi/GetTenantByTenantName?tenantName=" + tenantName,
				ApplicationConstant.AdminScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}
		return null;
	}

	@Override
	public Tenant GetTenantByNameOrNickName(String tenantName) {
		ServiceResult<Tenant> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<Tenant>>() {}, 
				ServiceName + ".GetTenantByNameOrNickName",
				AdminApiServiceUrl() + "TenantApi/GetTenantByNameOrNickName?tenantName=" + tenantName,
				ApplicationConstant.AdminScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}
		return null;
	}

	@Override
	public Tenant GetTenantEndWithDomainName(String domainName) {
		ServiceResult<Tenant> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<Tenant>>() {}, 
				ServiceName + ".GetTenantEndWithDomainName",
				AdminApiServiceUrl() + "TenantApi/GetTenantEndWithDomainName?domainName=" + domainName,
				ApplicationConstant.AdminScope, callback -> {
					return callback;
				}, failCallback -> {
					log.error(ServiceName + " throw error: " + failCallback.toString());
				}, true);

		if (result != null && result.isSuccess() && result.getResult() != null) {
			return result.getResult();
		}
		return null;
	}

}
