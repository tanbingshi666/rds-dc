package com.skysec.soc.rds.dc.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elastic")
@Data
public class ElasticSearchConfig {

    private String[] hosts;
    private String username;
    private String password;
    private String fingerprint;

    private int connectionTimeout;
    private int socketTimeout;

}
