package com.skysec.soc.rds.dc.controller;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import com.skysec.soc.rds.dc.pojo.vo.BaseQueryRequest;
import com.skysec.soc.rds.dc.pojo.vo.QueryResponse;
import com.skysec.soc.rds.dc.pojo.vo.Result;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;
import com.skysec.soc.rds.dc.service.SqlLocating;
import com.skysec.soc.rds.dc.utils.FutureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

public abstract class BaseSqlController<REQ extends BaseQueryRequest, T extends SqlQueryObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSqlController.class);

    @Autowired
    protected SqlLocating<T> sqlLocating;

    public Result<QueryResponse> doSqlQuery(REQ request) {
        T sqlObject = getSqlObject(request);
        if (sqlObject == null) {
            LOGGER.info("找不到对应的 SQL 配置, 请求参数为 {}", request);
            return Result.buildFailure("找不到对应的 SQL 配置");
        }

        QueryResponse queryResponse = null;
        try {
            if (sqlObject.isAsync()) {
                CompletableFuture<QueryResponse> future = CompletableFuture.supplyAsync(
                        () -> doSqlQuery(request, sqlObject),
                        FutureUtil.executorFuture.getExecutor());

                queryResponse = future.get();
            } else {
                queryResponse = doSqlQuery(request, sqlObject);
            }
        } catch (Exception e) {
            LOGGER.error("查询 rds-dc 错误 " + e);
            e.printStackTrace();
        }

        if (queryResponse == null) {
            return Result.buildFailure("查询 rds-dc 错误");
        }

        return Result.buildSuc(queryResponse);
    }

    protected T getSqlObject(REQ request) {
        return sqlLocating.locate(request.getSqlPath());
    }

    public QueryResponse doSqlQuery(REQ request, T sqlObject) {
        return new QueryResponse(doSqlExecution(request, sqlObject));
    }

    protected abstract SqlExecResult doSqlExecution(REQ request, T sqlObject);
}
