package com.skysec.soc.rds.dc.pojo.vo;

import lombok.Data;

@Data
public class DslQueryRequest extends BaseQueryRequest {

    private String dslPath;

    private String index;

    @Override
    public String getPath() {
        return getDslPath();
    }
}
