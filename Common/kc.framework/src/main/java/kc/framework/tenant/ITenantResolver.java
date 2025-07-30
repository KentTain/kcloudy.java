package kc.framework.tenant;

public interface ITenantResolver {
	/**
	 * 根据tenantName或url，获取相关的租户信息，如果为url，其实例如下：<br/>
	 * http(s)://www.domai.com/controller/action/query= <br/>
	 * http(s)://ctest.domai.com/controller/action/query= <br/>
	 */
	Tenant Resolve(String tenantNameOrUrl);
}
