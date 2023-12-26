package com.skysec.soc.rds.dc.datasource.postgres;

import com.skysec.soc.rds.dc.datasource.DataSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class PostgresSQLDataSource implements DataSourceProvider {

    public static final String PG_DATASOURCE_IDENTIFIED = "PostgresSQL";

    @Resource(name = "postgreSqlJdbcTemplate")
    private NamedParameterJdbcTemplate template;

    @Override
    public String getIdentified() {
        return PG_DATASOURCE_IDENTIFIED;
    }

    @Override
    public DataSource getDataSource() {
        return template.getJdbcTemplate().getDataSource();
    }

    @Override
    public List<Map<String, Object>> execute(String sql, Map<String, Object> params) {
        return template.queryForList(sql, new MapSqlParameterSource(params));
    }

}
