package com.skysec.soc.rds.dc.service.impl;

import com.skysec.soc.rds.dc.service.SqlLocating;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSqlLocatingService<T> implements SqlLocating<T> {

    protected ConcurrentHashMap<String, T> sqlList = new ConcurrentHashMap<>();

    @Override
    public T locate(String sqlPath) {
        return sqlList.get(sqlPath);
    }
}
