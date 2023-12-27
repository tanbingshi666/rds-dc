package com.skysec.soc.rds.dc.pojo.vo;

import lombok.Data;

import java.util.Map;

@Data
public abstract class BaseQueryRequest {

    protected Map<String, Object> params;

    public abstract String getPath();

}
