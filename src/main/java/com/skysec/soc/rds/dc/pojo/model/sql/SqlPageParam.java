package com.skysec.soc.rds.dc.pojo.model.sql;

import lombok.Data;

@Data
public class SqlPageParam {

    private Integer pageNo = -1;

    private Integer pageSize = 10;

}
