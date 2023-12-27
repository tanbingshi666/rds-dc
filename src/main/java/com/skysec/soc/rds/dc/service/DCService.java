package com.skysec.soc.rds.dc.service;

import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.pojo.vo.sql.SqlParam;
import com.skysec.soc.rds.dc.pojo.vo.sql.SqlExecResult;

import java.util.Map;

public interface DCService {

    SqlExecResult executeSQL(Config config, SqlParam sqlParams);

    Object executeDSL(String index, String dsl, Map<String, Object> params);

}
