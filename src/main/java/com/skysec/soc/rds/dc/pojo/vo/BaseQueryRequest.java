package com.skysec.soc.rds.dc.pojo.vo;

import lombok.Data;

import java.util.Map;

@Data
public class BaseQueryRequest {

    protected String sqlPath;

    protected Map<String, Object> params;



}
