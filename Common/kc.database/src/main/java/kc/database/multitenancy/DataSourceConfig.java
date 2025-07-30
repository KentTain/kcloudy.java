package kc.database.multitenancy;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@lombok.extern.slf4j.Slf4j
public class DataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource masterDatasource(){
        log.info("Setting up masterDatasource with :{}",dataSourceProperties.toString());
        HikariDataSource datasource = new HikariDataSource();
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setJdbcUrl(dataSourceProperties.getUrl());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        datasource.setPoolName(dataSourceProperties.getPoolName());
        datasource.setMaximumPoolSize(dataSourceProperties.getMaxPoolSize() > 0
                ? dataSourceProperties.getMaxPoolSize() : 15);
        datasource.setMinimumIdle(dataSourceProperties.getMinIdle() > 0
                ? dataSourceProperties.getMinIdle() : 5);
        datasource.setConnectionTimeout(dataSourceProperties.getConnectionTimeout() > 0
                ? dataSourceProperties.getConnectionTimeout() : 30000);
        datasource.setIdleTimeout(dataSourceProperties.getIdleTimeout() > 0
                ? dataSourceProperties.getIdleTimeout() : 0);
        log.info("Setup of masterDatasource successfully.");
        return datasource;
    }

}

