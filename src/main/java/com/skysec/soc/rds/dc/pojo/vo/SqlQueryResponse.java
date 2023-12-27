package com.skysec.soc.rds.dc.pojo.vo;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlExecResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SqlQueryResponse {

    private Long total;
    private Object list;

    public SqlQueryResponse(SqlExecResult result) {
        if (result != null) {
            setTotal(result.getTotal());
            setList(result.getResult());
        } else {
            setTotal(0L);
        }
    }

}
