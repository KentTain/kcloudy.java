package kc.database.multitenancy;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantContext;

/**
 * 这个类是由Hibernate提供的用于识别tenantId的类，当每次执行sql语句被拦截就会调用这个类中的方法来获取tenantId
 * @author lanyuanxiaoyao
 * @version 1.0
 * https://dzone.com/articles/spring-boot-hibernate-multitenancy-implementation
 */
@Component
public class MultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver{
	private Logger logger = LoggerFactory.getLogger(MultiTenantIdentifierResolver.class.getName());
    
	// 获取tenantId的逻辑在这个方法里面写
	@Override
    public String resolveCurrentTenantIdentifier() {
        Tenant tenant = TenantContext.getCurrentTenant();
        if (tenant != null) {
        	logger.debug("-----MultiTenantIdentifierResolver get tenant: " + tenant.getTenantName());
            return tenant.getTenantName();
        }
        
        logger.debug("-----MultiTenantIdentifierResolver default tenant: " + TenantDataSourceProvider.DEFAULT_TENANT_NAME);
        return TenantDataSourceProvider.DEFAULT_TENANT_NAME;
    }
	
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
