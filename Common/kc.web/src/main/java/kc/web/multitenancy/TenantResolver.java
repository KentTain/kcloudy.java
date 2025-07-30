package kc.web.multitenancy;

import kc.service.webapiservice.thridparty.impl.TenantUserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ITenantResolver;
import kc.framework.tenant.Tenant;
import kc.service.webapiservice.thridparty.ITenantUserApiService;

@Component
public class TenantResolver implements ITenantResolver {

    @Autowired
    private ITenantUserApiService tenantUserApiService;

    /**
     * 根据tenantName或url，获取相关的租户信息，如果为url，其实例如下：<br/>
     * http(s)://www.domai.com/controller/action/query= <br/>
     * http(s)://ctest.domai.com/controller/action/query= <br/>
     */
    @Override
    public Tenant Resolve(String tenantNameOrUrl) {
        if (tenantUserApiService == null)
            tenantUserApiService = new TenantUserApiService();
        String tenantName = new String(tenantNameOrUrl);
        if (StringExtensions.isUrl(tenantNameOrUrl))
            tenantName = StringExtensions.getTenantNameFromUrl(tenantNameOrUrl);

        if (!StringExtensions.isNullOrEmpty(tenantName)) {
            Tenant tenant = tenantUserApiService.GetTenantByName(tenantName);
            if (tenant != null)
                return tenant;
        }

        String domain = StringExtensions.getDomainNameFromUrl(tenantNameOrUrl);
        if (!StringExtensions.isNullOrEmpty(tenantName))
            return tenantUserApiService.GetTenantEndWithDomainName(domain);

        return null;
    }
}
