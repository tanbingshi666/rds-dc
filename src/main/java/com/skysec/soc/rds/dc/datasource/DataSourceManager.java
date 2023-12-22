package com.skysec.soc.rds.dc.datasource;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSourceManager {

    private final Map<String, DataSourceProvider> dataSources = new HashMap<>();

    public DataSourceManager(List<DataSourceProvider> dataSourceProviders) {
        for (DataSourceProvider dataSourceProvider : dataSourceProviders) {
            dataSources.put(dataSourceProvider.getIdentified(), dataSourceProvider);
        }
    }

    public DataSourceProvider getDataSource(String identified) {
        return dataSources.get(identified);
    }

}
