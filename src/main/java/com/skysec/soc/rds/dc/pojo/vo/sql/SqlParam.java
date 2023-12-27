package com.skysec.soc.rds.dc.pojo.vo.sql;

import lombok.Data;

import java.util.Map;

@Data
public class SqlParam {

    private Map<String, Object> params;

    private SqlPageParam pageParam;

}
