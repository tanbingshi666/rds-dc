package com.skysec.soc.rds.dc.datasource.dynamic;

import javax.sql.DataSource;
import java.util.Map;

public interface DynamicDataSourceProvider {

    DataSource defaultDataSource();

    Map<Object, Object> availableDataSource();

}
