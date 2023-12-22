package com.skysec.soc.rds.dc.pojo.vo;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlPageParam;
import lombok.Data;

@Data
public class QueryRequest extends BaseQueryRequest {

    private SqlPageParam page;

    @Override
    public String toString() {
        return "QueryRequest {" +
                "sqlPath ='" + getSqlPath() +
                ", params =" + getParams() +
                ", page =" + getPage() +
                '}';
    }

}
