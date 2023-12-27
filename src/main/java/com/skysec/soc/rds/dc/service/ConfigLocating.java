package com.skysec.soc.rds.dc.service;

public interface ConfigLocating<T> {

    T locate(String path);

    void cache(T t);
}
