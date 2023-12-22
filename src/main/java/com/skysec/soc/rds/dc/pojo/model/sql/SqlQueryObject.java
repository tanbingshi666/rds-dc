package com.skysec.soc.rds.dc.pojo.model.sql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqlQueryObject {

    private String id;
    private String dataSource;
    private String querySql;
    private boolean isPageQuery;
    private boolean isAsync;

}
