package com.skysec.soc.rds.dc.datasource;

import com.skysec.soc.rds.dc.datasource.elastic.ElasticSearchDataSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSourceManager {

    private final Map<String, DataSourceProvider> dataSources = new HashMap<>();

    private final ElasticSearchDataSource elasticSearchDataSource;

    public DataSourceManager(List<DataSourceProvider> dataSourceProviders,
                             ElasticSearchDataSource elasticSearchDataSource) {
        for (DataSourceProvider dataSourceProvider : dataSourceProviders) {
            dataSources.put(dataSourceProvider.getIdentified(), dataSourceProvider);
        }
        this.elasticSearchDataSource = elasticSearchDataSource;
    }

    public DataSourceProvider getDataSource(String identified) {
        return dataSources.get(identified);
    }

    public ElasticSearchDataSource getElasticSearchDataSource() {
        return elasticSearchDataSource;
    }

}
