package com.skysec.soc.rds.dc.pojo.model.sql;

import lombok.Data;

import java.util.List;

@Data
public class SqlExecResult {

    private Long total;
    private List<? extends Object> result;

}
