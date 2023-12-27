package com.skysec.soc.rds.dc.service.impl;

import com.skysec.soc.rds.dc.service.ConfigLocating;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConfigLocatingService<T> implements ConfigLocating<T> {

    protected ConcurrentHashMap<String, T> configList = new ConcurrentHashMap<>();

    @Override
    public T locate(String path) {
        return configList.get(path);
    }
}
