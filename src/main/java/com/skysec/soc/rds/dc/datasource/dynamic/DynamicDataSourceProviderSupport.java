package com.skysec.soc.rds.dc.datasource.dynamic;

import com.alibaba.druid.pool.DruidDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DynamicDataSourceProviderSupport implements DynamicDataSourceProvider {

    @Resource
    private Map<String, DruidDataSource> dataSources = new HashMap<>();

    @Override
    public DataSource defaultDataSource() {
        return dataSources.get(DynamicDataSourceEnum.DEFAULT.name());
    }

    @Override
    public Map<Object, Object> availableDataSource() {
        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        for (String key : dataSources.keySet()) {
            dataSourceMap.put(key, dataSources.get(key));
        }

        return dataSourceMap;
    }
}
