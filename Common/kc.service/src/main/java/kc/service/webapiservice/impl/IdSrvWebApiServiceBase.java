package kc.service.webapiservice.impl;

import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantContext;
import kc.service.WebApiServiceBase;

public abstract class IdSrvWebApiServiceBase extends WebApiServiceBase{

	@Override
	protected OAuth2ClientInfo getOAuth2ClientInfo() {
		Tenant tenant = TenantContext.getCurrentTenant();
		return super.getTenantOAuth2ClientInfo(tenant);
	}
	
	@Override
	protected String SSOServerUrl() {
		Tenant tenant = TenantContext.getCurrentTenant();
		String tenantName = tenant.getTenantName();
		return super.getTenantSSOServerUrl(tenantName);
	}
}
