package com.skysec.soc.rds.dc.controller;

import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.pojo.vo.BaseQueryRequest;
import com.skysec.soc.rds.dc.pojo.vo.SqlQueryResponse;
import com.skysec.soc.rds.dc.pojo.vo.Result;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;
import com.skysec.soc.rds.dc.service.ConfigLocating;
import com.skysec.soc.rds.dc.utils.FutureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

public abstract class BaseController<REQ extends BaseQueryRequest, T extends Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected ConfigLocating<T> configLocating;

    public Result<SqlQueryResponse> doSqlQuery(REQ request) {
        T config = getConfig(request);
        if (config == null) {
            LOGGER.info("找不到对应的 SQL 配置, 请求参数为 {}", request);
            return Result.buildFailure("找不到对应的 SQL 配置");
        }

        SqlQueryResponse sqlQueryResponse = null;
        try {
            if (config.isAsync()) {
                CompletableFuture<SqlQueryResponse> future = CompletableFuture.supplyAsync(
                        () -> doSqlQuery(request, config),
                        FutureUtil.executorFuture.getExecutor());

                sqlQueryResponse = future.get();
            } else {
                sqlQueryResponse = doSqlQuery(request, config);
            }
        } catch (Exception e) {
            LOGGER.error("查询 rds-dc SQL 错误 " + e);
            e.printStackTrace();
        }

        if (sqlQueryResponse == null) {
            return Result.buildFailure("查询 rds-dc SQL 错误");
        }

        return Result.buildSuc(sqlQueryResponse);
    }

    public Result<Object> doDSlQuery(REQ request) {
        T config = getConfig(request);
        if (config == null) {
            LOGGER.info("找不到对应的 DSL 配置, 请求参数为 {}", request);
            return Result.buildFailure("找不到对应的 DSL 配置");
        }

        Object object = null;
        try {
            if (config.isAsync()) {
                CompletableFuture<Object> future = CompletableFuture.supplyAsync(
                        () -> doDslQuery(request, config),
                        FutureUtil.executorFuture.getExecutor());

                object = future.get();
            } else {
                object = doDslQuery(request, config);
            }
        } catch (Exception e) {
            LOGGER.error("查询 rds-dc DSL 错误 " + e);
            e.printStackTrace();
        }

        if (object == null) {
            return Result.buildFailure("查询 rds-dc DSL 错误");
        }

        return Result.buildSuc(object);

    }

    private Object doDslQuery(REQ request, T config) {
        return doDslExecution(request, config);
    }

    protected T getConfig(REQ request) {
        return configLocating.locate(request.getPath());
    }

    private SqlQueryResponse doSqlQuery(REQ request, T sqlObject) {
        return new SqlQueryResponse(doSqlExecution(request, sqlObject));
    }

    protected SqlExecResult doSqlExecution(REQ request, T sqlObject) {
        return null;
    }

    protected Object doDslExecution(REQ request, T config) {
        return null;
    }

}
