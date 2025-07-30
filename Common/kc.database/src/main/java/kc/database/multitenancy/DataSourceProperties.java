package kc.database.multitenancy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.datasource")
public class DataSourceProperties {

    private String url;

    private String password;

    private String username;

    private String driverClassName;

    private long connectionTimeout;

    private int maxPoolSize;

    private long idleTimeout;

    private int minIdle;

    private String poolName;


    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("MasterDatabaseProperties [ url=")
                .append(url)
                .append(", username=")
                .append(username)
                .append(", password=")
                .append(password)
                .append(", driverClassName=")
                .append(driverClassName)
                .append(", connectionTimeout=")
                .append(connectionTimeout)
                .append(", maxPoolSize=")
                .append(maxPoolSize)
                .append(", idleTimeout=")
                .append(idleTimeout)
                .append(", minIdle=")
                .append(minIdle)
                .append(", poolName=")
                .append(poolName)
                .append("]");
        return builder.toString();
    }

}

