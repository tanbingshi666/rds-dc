package com.skysec.soc.rds.dc.pojo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Config {

    private String id;
    private String dataSource;
    private String config;
    private boolean isPage;
    private boolean isAsync;

}
