package com.skysec.soc.rds.dc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.connect.timeout:30000}")
    private long restConnectTimeout;

    @Value("${rest.read.timeout:30000}")
    private long restReadTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.of(restConnectTimeout, ChronoUnit.MILLIS))
                .setReadTimeout(Duration.of(restReadTimeout, ChronoUnit.MILLIS))
                .build();
    }
}
