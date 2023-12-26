package com.skysec.soc.rds.dc.datasource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface DataSourceProvider {

    String getIdentified();

    DataSource getDataSource();

    List<Map<String, Object>> execute(String sql, Map<String, Object> params);

}
