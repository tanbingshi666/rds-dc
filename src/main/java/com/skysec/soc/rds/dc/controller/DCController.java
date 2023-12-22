package com.skysec.soc.rds.dc.controller;

import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceContextHolder;
import com.skysec.soc.rds.dc.datasource.dynamic.DynamicDataSourceEnum;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlParam;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import com.skysec.soc.rds.dc.pojo.vo.QueryRequest;
import com.skysec.soc.rds.dc.pojo.vo.QueryResponse;
import com.skysec.soc.rds.dc.pojo.vo.Result;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;
import com.skysec.soc.rds.dc.service.DCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DCController extends BaseSqlController<QueryRequest, SqlQueryObject> {

    private final DCService dcService;

    public DCController(DCService dcService) {
        this.dcService = dcService;
    }

    @PostMapping("/query/rds/sqlQuery")
    public Result<QueryResponse> sqlQuery(@RequestBody QueryRequest queryRequest) {
        return doSqlQuery(queryRequest);
    }

    @Override
    protected SqlExecResult doSqlExecution(QueryRequest request, SqlQueryObject sqlObject) {
        SqlParam sqlParams = new SqlParam();
        sqlParams.setParams(request.getParams());
        sqlParams.setPageParam(request.getPage());
        return dcService.execute(sqlObject, sqlParams);
    }

    @Autowired
    NamedParameterJdbcTemplate dynamicNamedParameterJdbcTemplate;

    // 测试动态多数据源
    @PostMapping("/test/datasource/{datasourceName}")
    public Object testDynamicDataSource(@PathVariable("datasourceName") String datasourceName) {

        if (DynamicDataSourceEnum.MYSQL.getDataSourceName().equals(datasourceName)) {
            DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.MYSQL.getDataSourceName());
            String mysql = "select * from demo";
            List<Map<String, Object>> mysqlResult = dynamicNamedParameterJdbcTemplate.queryForList(mysql, new MapSqlParameterSource());
            DynamicDataSourceContextHolder.removeDataSource();
            return mysqlResult;
        }

        if (DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName().equals(datasourceName)) {
            DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName());
            String postgres = "select * from t_siem_threat_scrapy limit 3";
            List<Map<String, Object>> postgresResult = dynamicNamedParameterJdbcTemplate.queryForList(postgres, new MapSqlParameterSource());
            DynamicDataSourceContextHolder.removeDataSource();
            return postgresResult;
        }

        return "没有对应的数据源";
    }
}
