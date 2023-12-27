package com.skysec.soc.rds.dc.pojo.vo.dsl;

import com.skysec.soc.rds.dc.pojo.vo.BaseQueryRequest;
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
