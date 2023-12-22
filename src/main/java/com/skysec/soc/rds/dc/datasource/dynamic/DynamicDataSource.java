package com.skysec.soc.rds.dc.datasource.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 实现动态数据源，根据 AbstractRoutingDataSource 路由到不同数据源中
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final DynamicDataSourceProvider dynamicDataSourceProvider;

    public DynamicDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        this.dynamicDataSourceProvider = dynamicDataSourceProvider;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSource();
    }

    @Override
    public void afterPropertiesSet() {
        super.setDefaultTargetDataSource(dynamicDataSourceProvider.defaultDataSource());
        super.setTargetDataSources(dynamicDataSourceProvider.availableDataSource());
        super.afterPropertiesSet();
    }
}
