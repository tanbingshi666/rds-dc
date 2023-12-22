package com.skysec.soc.rds.dc.service;

public interface SqlLocating<T> {

    T locate(String sqlPath);

    void cache(T t);
}
