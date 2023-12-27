package com.skysec.soc.rds.dc.controller;

import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.pojo.vo.dsl.DslQueryRequest;
import com.skysec.soc.rds.dc.pojo.vo.Result;
import com.skysec.soc.rds.dc.service.DCService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DSLController extends BaseController<DslQueryRequest, Config> {

    private final DCService dcService;

    public DSLController(DCService dcService) {
        this.dcService = dcService;
    }

    @PostMapping("/query/rds/dslQuery")
    public Result<Object> dslQuery(@RequestBody DslQueryRequest dslQueryRequest) {
        return doDSlQuery(dslQueryRequest);
    }

    @Override
    protected Object doDslExecution(DslQueryRequest request, Config config) {
        String index = request.getIndex();
        String dsl = config.getConfig();
        Map<String, Object> params = request.getParams();
        return dcService.executeDSL(index, dsl, params);
    }
}
