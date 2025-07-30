package kc.database.multitenancy;

import com.zaxxer.hikari.HikariDataSource;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantContext;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 指定了 ConnectionProvider，即 Hibernate 需要知道如何以租户特有的方式获取数据连接
 *
 * @author tianchangjun
 */
@Component
@Configuration
@lombok.extern.slf4j.Slf4j
public class MsSqlMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final long serialVersionUID = -7522287771874314380L;

    /**
     * 返回默认的数据源
     */
    @Override
    protected DataSource selectAnyDataSource() {
        System.out.println("-----MsSqlMultiTenantConnectionProviderImpl selectAnyDataSource by default tenant: "
                + TenantDataSourceProvider.DEFAULT_TENANT_NAME);
        return TenantDataSourceProvider.getTenantDataSource(TenantDataSourceProvider.DEFAULT_TENANT_NAME);
    }

    @Autowired(required = false)
    private kc.framework.tenant.ITenantResolver tenantResolver;

    /**
     * 根据tenantIdentifier返回指定数据源
     *
     * @param tenantIdentifier
     * @return 数据源
     */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        System.out.println("-----MsSqlMultiTenantConnectionProviderImpl selectDataSource by tenant: " + tenantIdentifier);

        DataSource ds = TenantDataSourceProvider.getTenantDataSource(tenantIdentifier);
        if (ds instanceof HikariDataSource) {
            HikariDataSource hikariDs = (HikariDataSource) ds;
            if (hikariDs.getPoolName().equalsIgnoreCase(tenantIdentifier))
                return ds;
        }

        Tenant tenant = TenantContext.getCurrentTenant();
        if (tenant == null)
            tenant = tenantResolver.Resolve(tenantIdentifier);

        if (tenant == null)
            throw new NullPointerException(String.format("未找到相关租户: %s 的数据源", tenantIdentifier));

        TenantDataSourceProvider.addDataSource(tenant);

        return TenantDataSourceProvider.getTenantDataSource(tenantIdentifier);
    }
}