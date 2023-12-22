package com.skysec.soc.rds.dc.service.impl;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import org.springframework.stereotype.Service;

@Service
public class SqlLocatingService extends AbstractSqlLocatingService<SqlQueryObject> {

    @Override
    public void cache(SqlQueryObject sqlQueryObject) {
        sqlList.put(sqlQueryObject.getId(), sqlQueryObject);
    }

}
