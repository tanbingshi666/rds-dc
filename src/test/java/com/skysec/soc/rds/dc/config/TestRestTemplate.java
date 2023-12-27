package com.skysec.soc.rds.dc.config;

import cn.hutool.json.JSONObject;
import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceContextHolder;
import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceEnum;
import com.skysec.soc.rds.dc.datasource.elastic.ElasticSearchDataSource;
import com.skysec.soc.rds.dc.metadata.elastic.SyncElasticSearchMetadata;
import com.skysec.soc.rds.dc.metadata.postgres.SyncPostgresSQLMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    @Autowired
    SyncPostgresSQLMetadata syncPostgresSQLMetadata;

    @Test
    public void testSyncPGMetadata() {
        syncPostgresSQLMetadata.doSyncMetadata();
    }

    @Autowired
    ElasticSearchDataSource elasticSearchDataSource;

    @Test
    public void testElasticSearch() {
        // final List<JSONObject> indexInfo = elasticSearchDataSource.listIndex();
        // System.out.println(indexInfo);

        final JSONObject indexMappingJson = elasticSearchDataSource.getIndexMapping("event");
        System.out.println(indexMappingJson.toJSONString(0));
    }

    @Autowired
    SyncElasticSearchMetadata syncElasticSearchMetadata;

    @Test
    public void testSyncESMetadata() {
        syncElasticSearchMetadata.doSyncMetadata();
    }

    @Test
    public void testESDSLExecute() {

        String index = "event";
        String dsl = "{\n" +
                "    \"query\": {\n" +
                "        \"range\": {\n" +
                "            \"storagetime\": {\n" +
                "                \"gte\": #{startTime},\n" +
                "                \"lte\": #{endTime}\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", 1698742172454L);
        params.put("endTime", 1698742182454L);

        final JSONObject json = elasticSearchDataSource.execute(index, dsl, params);
        System.out.println(json);

    }
}
