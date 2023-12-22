package com.skysec.soc.rds.dc.datasource.postgres;

import com.skysec.soc.rds.dc.datasource.DataSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class PostgresSQLDataSource implements DataSourceProvider {

    @Resource(name = "postgreSqlJdbcTemplate")
    private NamedParameterJdbcTemplate template;

    @Override
    public String getIdentified() {
        return "PostgresSQL";
    }

    @Override
    public List<Map<String, Object>> execute(String sql, Map<String, Object> params) {
        return template.queryForList(sql, new MapSqlParameterSource(params));
    }

}
