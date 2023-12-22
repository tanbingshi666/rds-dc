package com.skysec.soc.rds.dc.datasource.config;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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

}
