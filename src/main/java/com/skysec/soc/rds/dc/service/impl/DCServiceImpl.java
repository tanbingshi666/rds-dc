package com.skysec.soc.rds.dc.service.impl;

import com.skysec.soc.rds.dc.datasource.DataSourceManager;
import com.skysec.soc.rds.dc.datasource.DataSourceProvider;
import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlParam;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;
import com.skysec.soc.rds.dc.service.DCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DCServiceImpl implements DCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCServiceImpl.class);

    private final DataSourceManager dataSourceManager;

    public DCServiceImpl(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    @Override
    public SqlExecResult executeSQL(Config config, SqlParam sqlParams) {
        SqlExecResult execResult = new SqlExecResult();

        DataSourceProvider dataSource = dataSourceManager.getDataSource(config.getDataSource());
        if (config.isPage()) {
            if (sqlParams.getParams() == null) {
                sqlParams.setParams(new HashMap<>());
            }
            sqlParams.getParams().put("pageNo", sqlParams.getPageParam().getPageNo());
            sqlParams.getParams().put("pageSize", sqlParams.getPageParam().getPageSize());
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Map<String, Object>> queryResult = dataSource.execute(config.getConfig(), sqlParams.getParams());
        stopWatch.stop();
        LOGGER.info("执行 SQL = [{}], 花费时间 = {} ms", config.getConfig(), stopWatch.getTotalTimeMillis());

        if (CollectionUtils.isEmpty(queryResult)) {
            execResult.setTotal(0L);
            execResult.setResult(new ArrayList<>());
        } else {
            execResult.setTotal((long) queryResult.size());
            execResult.setResult(queryResult);
        }

        return execResult;
    }

    @Override
    public Object executeDSL(String index, String dsl, Map<String, Object> params) {
        return dataSourceManager.getElasticSearchDataSource().execute(index, dsl, params);
    }

}
