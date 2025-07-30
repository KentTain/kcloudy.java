package kc.service.webapiservice.thridparty;

import kc.framework.tenant.Tenant;
import kc.service.base.ServiceResult;

public interface ITenantUserApiService {

	ServiceResult<Boolean> ExistTenantName(String tenantName);

	Tenant GetTenantByName(String tenantName);

	Tenant GetTenantByNameOrNickName(String tenantName);

	Tenant GetTenantEndWithDomainName(String domainName);

}
