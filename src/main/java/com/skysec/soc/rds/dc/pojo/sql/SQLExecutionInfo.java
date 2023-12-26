package com.skysec.soc.rds.dc.pojo.sql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SQLExecutionInfo {

    @Builder.Default
    private Boolean success = true;

    private String error;

    @Builder.Default
    private List<String> columns = new ArrayList<>();

    @Builder.Default
    private List<List<Object>> rows = new ArrayList<>();
}
