package com.skysec.soc.rds.dc.service;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlParam;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;

public interface DCService {

    SqlExecResult execute(SqlQueryObject sqlQueryObject, SqlParam sqlParams);

}
