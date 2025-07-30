package kc.database.multitenancy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kc.database.TenantConnection;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 根据租户ID来提供对应的数据源
 *
 * @author tianchangjun
 * @version 1.0
 */
@Component
public class TenantDataSourceProvider {
    private static Logger logger = LoggerFactory.getLogger(TenantDataSourceProvider.class.getName());
    static String DEFAULT_TENANT_NAME = TenantConstant.TestTenantName;

    private static String defaultDatabaseJdbcUrl = "jdbc:sqlserver://rm-8vb7ip9632ck1tt4l.mssql.zhangbei.rds.aliyuncs.com:3433;databaseName=MSSqlKCContext";
    private static String defaultDatabaseDriverClass = TenantConnection.DEFAULT_SQLSERVER_DRIVER_CLASS;
    private static String defaultDatabaseUserName = TenantConstant.TestTenantName;
    private static String defaultDatabasePassword = "NG6lJCNxSxZHrihmlyXS";

    // 使用一个map来存储我们租户和对应的数据源，租户和数据源的信息就是从我们的tenant_info表中读出来
    private static ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public static final String DEFAULT_POOL_CONFIG = "default";

    /*
     * 静态建立一个数据源，也就是我们的默认数据源，假如我们的访问信息里面没有指定tenantId，就使用默认数据源。
     * 在我这里默认数据源是cloud_config，实际上你可以指向你们的公共信息的库，或者拦截这个操作返回错误。
     */
    static {
        initDataSource();
    }

    // 根据传进来的tenantId决定返回的数据源
    static DataSource getTenantDataSource(String tenantId) {
        String key = dataSourceMap.containsKey(tenantId)
                ? tenantId
                : DEFAULT_POOL_CONFIG;
        DataSource result = dataSourceMap.get(key);
//        if (result instanceof HikariDataSource) {
//            HikariDataSource hikariDs = (HikariDataSource) result;
//            System.out.println("-----TenantDataSourceProvider GetDataSource: " + key + ", connect: " + hikariDs.getJdbcUrl());
//        }

        return result;
    }

    // 初始化的时候用于添加数据源的方法
    public static void addDataSource(Tenant tenantInfo) {
        //System.out.println("-----TenantDataSourceProvider ConnectionString: " + tenantInfo.ConnectionString());

        String dbType = TenantConnection.DEFAULT_SQLSERVER_DRIVER_CLASS;
        switch (tenantInfo.getDatabaseType()){
            case MySql:
                dbType = TenantConnection.DEFAULT_MYSQL_DRIVER_CLASS;
                break;
            case SqlServer:
                dbType = TenantConnection.DEFAULT_SQLSERVER_DRIVER_CLASS;
                break;
            case Oracle:
                dbType = TenantConnection.DEFAULT_ORACLE_DRIVER_CLASS;
                break;
            case PostgreSQL:
                dbType = TenantConnection.DEFAULT_POSTGRESQL_DRIVER_CLASS;
                break;
        }

        //com.zaxxer.hikari
        HikariConfig config = getDefaultHikariConfig();
        config.setPoolName(tenantInfo.getTenantName());
        if (tenantInfo.getDatabaseType() == kc.framework.enums.DatabaseType.MySql)
            config.setSchema(tenantInfo.getTenantName());
        config.setDriverClassName(dbType);
        config.setUsername(tenantInfo.getTenantName());
        config.setPassword(tenantInfo.DatabasePassword());
        config.setJdbcUrl(tenantInfo.ConnectionString());

        HikariDataSource ds = new HikariDataSource(config);

        dataSourceMap.put(tenantInfo.getTenantName(), ds);
    }

    // 清除数据源，并保留默认的数据源设置
    public static void clearDataSource() {
        dataSourceMap.clear();

        initDataSource();
    }

    private static void initDataSource() {
        //com.zaxxer.hikari
        HikariConfig config = getDefaultHikariConfig();

        HikariDataSource ds = new HikariDataSource(config);

        dataSourceMap.put(DEFAULT_POOL_CONFIG, ds);
    }

    private static HikariConfig getDefaultHikariConfig() {
        String driverClass = getValueFromYamlKey("spring.datasource.driver-class-name");
        if (StringUtils.hasLength(driverClass))
            defaultDatabaseDriverClass = driverClass;
        String dbUrl = getValueFromYamlKey("spring.datasource.url");
        if (StringUtils.hasLength(dbUrl))
            defaultDatabaseJdbcUrl = dbUrl;
        String dbUserName = getValueFromYamlKey("spring.datasource.username");
        if (StringUtils.hasLength(dbUserName))
            defaultDatabaseUserName = dbUserName;
        String dbPassword = getValueFromYamlKey("spring.datasource.password");
        if (StringUtils.hasLength(dbPassword))
            defaultDatabasePassword = dbPassword;

//        System.out.println(String.format("=====%s==%s==%s==%s=======",
//                defaultDatabaseDriverClass, defaultDatabaseJdbcUrl, defaultDatabaseUserName, defaultDatabasePassword));
        HikariConfig config = new HikariConfig();
        config.setPoolName(DEFAULT_POOL_CONFIG);
        //config.setSchema(DEFAULT_TENANT_NAME);
        config.setDriverClassName(defaultDatabaseDriverClass);
        config.setJdbcUrl(defaultDatabaseJdbcUrl);
        config.setUsername(defaultDatabaseUserName);
        config.setPassword(defaultDatabasePassword);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1000);
        config.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        config.addDataSourceProperty("dataSource.prepStmtCacheSize", "100");
        config.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

        return config;
    }

    /**
     *  获取Web应用的application.yml文件的配置值
     * @param keyPath application.yml文件的配置Key，例如：spring.profiles.active
     * @return application.yml文件的配置值
     */
    public static String getValueFromYamlKey(String keyPath){
        Resource resource = new ClassPathResource("application.yml");
        String result = getValueFromYamlKey(resource, keyPath);
        if (StringUtils.hasLength(result))
            return result;

        String active = getValueFromYamlKey(resource, "spring.profiles.active");
        if (!StringUtils.hasLength(active))
            active = "dev";

        resource = new ClassPathResource("application-" + active + ".yml");
        return getValueFromYamlKey(resource, keyPath);
    }

    private static String getValueFromYamlKey(Resource resource, String keyPath) {
        try {
            List<PropertySource<?>> load = new YamlPropertySourceLoader().load(keyPath, resource);
            if (load == null || load.size() <= 0)
                return null;
            return load.get(0).getProperty(keyPath) == null ? "" : load.get(0).getProperty(keyPath).toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}