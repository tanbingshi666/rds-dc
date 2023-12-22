package com.skysec.soc.rds.dc.pojo.vo;

import com.skysec.soc.rds.dc.constant.Constant;
import lombok.Getter;

@Getter
public enum ResultStatus {

    SUCCESS(Constant.SUCCESS, "成功"),
    FAIL(Constant.FAIL, "失败");

    private final int code;

    private final String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
