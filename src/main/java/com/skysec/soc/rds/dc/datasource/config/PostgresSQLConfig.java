package com.skysec.soc.rds.dc.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "postgres")
@Data
public class PostgresSQLConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private String maxActive;
    private String initialSize;
    private String maxWait;
    private String minIdle;

    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;

    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;

    private String poolPreparedStatements;
    private String maxOpenPreparedStatements;

    private boolean asyncInit;


}
