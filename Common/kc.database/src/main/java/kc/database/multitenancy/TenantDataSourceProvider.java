package kc.database.multitenancy;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kc.database.TenantConnection;
import kc.framework.security.EncryptPasswordUtil;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;

//import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 根据租户ID来提供对应的数据源
 *
 * @author tianchangjun
 * @version 1.0
 */
@Component
@lombok.extern.slf4j.Slf4j
public class TenantDataSourceProvider {
    // private static Logger logger =
    // LoggerFactory.getLogger(TenantDataSourceProvider.class.getName());
    static String DEFAULT_TENANT_NAME = TenantConstant.TestTenantName;

    private static String defaultDatabaseJdbcUrl = "jdbc:sqlserver://121.89.220.143,1433;databaseName=MSSqlKCContext";
    private static String defaultDatabaseDriverClass = TenantConnection.DEFAULT_SQLSERVER_DRIVER_CLASS;
    private static String defaultDatabaseUserName = "sa";
    private static String defaultDatabasePassword = "Hcqqkeum+lPvQlPHyHOhM33xffnXWK2P";

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
        String key = dataSourceMap.containsKey(tenantId) ? tenantId : DEFAULT_POOL_CONFIG;
        DataSource result = dataSourceMap.get(key);
        // if (result instanceof HikariDataSource) {
        //     HikariDataSource hikariDs = (HikariDataSource) result;
        //     log.debug(String.format("===getTenantDataSource==%s==%s==", key, hikariDs.getJdbcUrl()));
        // }

        return result;
    }

    // 初始化的时候用于添加数据源的方法
    public static void addDataSource(Tenant tenantInfo) {
        log.info("-----TenantDataSourceProvider addDataSource: " + tenantInfo.ConnectionString());

        String dbType = TenantConnection.DEFAULT_SQLSERVER_DRIVER_CLASS;
        switch (tenantInfo.getDatabaseType()) {
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
        default:
            break;
        }

        // com.zaxxer.hikari
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
        // com.zaxxer.hikari
        HikariConfig config = getDefaultHikariConfig();

        HikariDataSource ds = new HikariDataSource(config);

        dataSourceMap.put(DEFAULT_POOL_CONFIG, ds);
    }

    private static HikariConfig getDefaultHikariConfig() {
        String driverClass = getValueFromYamlKey("spring.datasource.driver-class-name");
        if (ObjectUtils.isEmpty(driverClass))
            driverClass = defaultDatabaseDriverClass;
        String dbUrl = getValueFromYamlKey("spring.datasource.url");
        if (ObjectUtils.isEmpty(dbUrl))
            dbUrl = defaultDatabaseJdbcUrl;
        String dbUserName = getValueFromYamlKey("spring.datasource.username");
        if (ObjectUtils.isEmpty(dbUserName))
            dbUserName = defaultDatabaseUserName;
        String dbPassword = getValueFromYamlKey("spring.datasource.password");
        if (ObjectUtils.isEmpty(dbPassword))
            dbPassword = defaultDatabasePassword;
        String encryptKey = getValueFromYamlKey("GlobalConfig.EncryptKey");
        if (ObjectUtils.isEmpty(encryptKey))
            encryptKey = EncryptPasswordUtil.DEFAULT_Key;

        log.debug(String.format("===getDefaultHikariConfig driverClass：%s，dbUrl：%s，dbUserName：%s，dbPassword：%s",
                driverClass, dbUrl, dbUserName, dbPassword));
        String decryptDbPasswrod = EncryptPasswordUtil.DecryptPassword(dbPassword, encryptKey);

        HikariConfig config = new HikariConfig();
        config.setPoolName(DEFAULT_POOL_CONFIG);
        // config.setSchema(DEFAULT_TENANT_NAME);
        config.setDriverClassName(driverClass);
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUserName);
        config.setPassword(decryptDbPasswrod);
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
     * 获取Web应用的application.yml文件的配置值
     * 
     * @param keyPath application.yml文件的配置Key，例如：spring.profiles.active
     * @return application.yml文件的配置值
     */
    public static String getValueFromYamlKey(String keyPath) {
        // 1. 首先尝试从环境变量或系统属性中获取
        String envValue = System.getenv(keyPath.replace('.', '_').toUpperCase());
        if (StringUtils.hasText(envValue)) {
            log.debug("===Found value for {} in environment variables", keyPath);
            return envValue;
        }

        // 2. 获取当前激活的 profile
        String activeProfile = System.getProperty("spring.profiles.active");
        if (!StringUtils.hasText(activeProfile)) {
            activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
            if (!StringUtils.hasText(activeProfile)) {
                // 3. 从 application.yml 中获取激活的 profile
                Resource resource = new ClassPathResource("application.yml");
                activeProfile = getValueFromYamlKey(resource, "spring.profiles.active");
                if (!StringUtils.hasText(activeProfile)) {
                    activeProfile = "dev"; // 默认值
                }
            }
        }

        log.debug("===Loading configuration for profile: {}", activeProfile);

        // 4. 尝试从 application-{profile}.yml 中获取
        String profileSpecificFile = "application-" + activeProfile + ".yml";
        try {
            Resource profileResource = new ClassPathResource(profileSpecificFile);
            if (profileResource.exists()) {
                String value = getValueFromYamlKey(profileResource, keyPath);
                if (StringUtils.hasText(value)) {
                    return value;
                }
            }
        } catch (Exception e) {
            log.error("===Error loading profile-specific config: " + profileSpecificFile, e);
        }

        // 5. 最后尝试从 application.yml 中获取
        Resource defaultResource = new ClassPathResource("application.yml");
        return getValueFromYamlKey(defaultResource, keyPath);
    }

    private static String getValueFromYamlKey(Resource resource, String keyPath) {
        try {
            List<PropertySource<?>> load = new YamlPropertySourceLoader().load(keyPath, resource);
            if (load == null || load.size() <= 0)
                return null;
            return load.get(0) == null || load.get(0).getProperty(keyPath) == null ? ""
                    : load.get(0).getProperty(keyPath).toString();
        } catch (IOException ex) {
            log.error("===Error loading profile-specific config: " + resource.getFilename(), ex);
            return null;
        }
    }
}