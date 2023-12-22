package com.skysec.soc.rds.dc.datasource;

import java.util.List;
import java.util.Map;

public interface DataSourceProvider {

    String getIdentified();

    List<Map<String, Object>> execute(String sql, Map<String, Object> params);

}
