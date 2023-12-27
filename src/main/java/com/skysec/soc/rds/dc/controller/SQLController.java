package com.skysec.soc.rds.dc.controller;

import com.skysec.soc.rds.dc.pojo.vo.sql.SqlParam;
import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.pojo.vo.sql.SqlQueryRequest;
import com.skysec.soc.rds.dc.pojo.vo.sql.SqlQueryResponse;
import com.skysec.soc.rds.dc.pojo.vo.Result;
import com.skysec.soc.rds.dc.pojo.vo.sql.SqlExecResult;
import com.skysec.soc.rds.dc.service.DCService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SQLController extends BaseController<SqlQueryRequest, Config> {

    private final DCService dcService;

    public SQLController(DCService dcService) {
        this.dcService = dcService;
    }

    @PostMapping("/query/rds/sqlQuery")
    public Result<SqlQueryResponse> sqlQuery(@RequestBody SqlQueryRequest sqlQueryRequest) {
        return doSqlQuery(sqlQueryRequest);
    }

    @Override
    protected SqlExecResult doSqlExecution(SqlQueryRequest request, Config sqlObject) {
        SqlParam sqlParams = new SqlParam();
        sqlParams.setParams(request.getParams());
        sqlParams.setPageParam(request.getPage());
        return dcService.executeSQL(sqlObject, sqlParams);
    }

    // todo 测试动态多数据源
//    @Autowired
//    NamedParameterJdbcTemplate dynamicNamedParameterJdbcTemplate;
//    @PostMapping("/test/datasource/{datasourceName}")
//    public Object testDynamicDataSource(@PathVariable("datasourceName") String datasourceName) {
//
//        if (DynamicDataSourceEnum.MYSQL.getDataSourceName().equals(datasourceName)) {
//            DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.MYSQL.getDataSourceName());
//            String mysql = "select * from demo";
//            List<Map<String, Object>> mysqlResult = dynamicNamedParameterJdbcTemplate.queryForList(mysql, new MapSqlParameterSource());
//            DynamicDataSourceContextHolder.removeDataSource();
//            return mysqlResult;
//        }
//
//        if (DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName().equals(datasourceName)) {
//            DynamicDataSourceContextHolder.setDataSource(DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName());
//            String postgres = "select * from t_siem_threat_scrapy limit 3";
//            List<Map<String, Object>> postgresResult = dynamicNamedParameterJdbcTemplate.queryForList(postgres, new MapSqlParameterSource());
//            DynamicDataSourceContextHolder.removeDataSource();
//            return postgresResult;
//        }
//
//        return "没有对应的数据源";
//    }
}
