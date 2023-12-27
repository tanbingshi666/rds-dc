package com.skysec.soc.rds.dc.datasource.config;

import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.transport.TransportUtils;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean(name = "postgreSqlJdbcTemplate")
    public NamedParameterJdbcTemplate postgreSqlJdbcTemplate(PostgresSQLConfig postgreSQLConfig) {

        Map<String, Object> map = BeanUtil.beanToMap(postgreSQLConfig);
        DataSource dataSource = null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(map);
            return new NamedParameterJdbcTemplate(dataSource);
        } catch (Exception e) {
            LOGGER.error("初始化 PostgreSQL 数据源错误...");
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public RestClient getElasticsearchClient(ElasticSearchConfig elasticSearchConfig) {
        final String[] hosts = elasticSearchConfig.getHosts();
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            httpHosts[i] = HttpHost.create(hosts[i]);
        }

        RestClientBuilder restClientBuilder =
                RestClient.builder(httpHosts)
                        .setRequestConfigCallback(
                                requestConfigBuilder ->
                                        requestConfigBuilder
                                                .setConnectionRequestTimeout(elasticSearchConfig.getConnectionTimeout())
                                                .setSocketTimeout(elasticSearchConfig.getSocketTimeout()));

        restClientBuilder.setHttpClientConfigCallback(
                httpClientBuilder -> {
                    if (StringUtils.hasLength(elasticSearchConfig.getUsername()) && StringUtils.hasLength(elasticSearchConfig.getPassword())) {
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(
                                AuthScope.ANY,
                                new UsernamePasswordCredentials(elasticSearchConfig.getUsername(), elasticSearchConfig.getPassword()));
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }

                    if (elasticSearchConfig.getFingerprint() != null) {
                        httpClientBuilder.setSSLContext(
                                TransportUtils.sslContextFromCaFingerprint(
                                        elasticSearchConfig.getFingerprint()));
                    } else {
                        try {
                            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy()).build();
                            httpClientBuilder.setSSLContext(sslContext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return httpClientBuilder;
                });

        return restClientBuilder.build();
    }

}
