package com.skysec.soc.rds.dc.config;

import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceContextHolder;
import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRestTemplate {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    NamedParameterJdbcTemplate dynamicNamedParameterJdbcTemplate;

    @Test
    public void hello() {
        System.out.println("Hello Spring Boot Test");
    }

    @Test
    public void testRestTemplate() {
        String url = "http://hadoop101:8848/nacos/v2/cs/history/configs?namespaceId=public";
        Object response = restTemplate.getForObject(url, Object.class);
        System.out.println(response);
    }

    @Test
    public void testDynamicDataSource() {
        DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.MYSQL.getDataSourceName());
        String mysql = "select * from demo";
        List<Map<String, Object>> mysqlResult = dynamicNamedParameterJdbcTemplate.queryForList(mysql, new MapSqlParameterSource());
        System.out.println(mysqlResult);
        DynamicDataSourceContextHolder.removeDataSource();

        DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName());
        String postgres = "select * from t_siem_threat_scrapy limit 3";
        List<Map<String, Object>> postgresResult = dynamicNamedParameterJdbcTemplate.queryForList(postgres, new MapSqlParameterSource());
        System.out.println(postgresResult);
        DynamicDataSourceContextHolder.removeDataSource();

    }
}
