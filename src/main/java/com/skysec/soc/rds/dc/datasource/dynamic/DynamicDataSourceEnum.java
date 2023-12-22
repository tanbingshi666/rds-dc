package com.skysec.soc.rds.dc.datasource.dynamic;

import lombok.Getter;

public enum DynamicDataSourceEnum {

    DEFAULT("default"),

    MYSQL("MySQL"),

    POSTGRESSQL("PostgresSQL"),

    DORIS("Doris");

    @Getter
    private final String dataSourceName;

    DynamicDataSourceEnum(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

}
