package com.skysec.soc.rds.dc.pojo.vo;

import com.skysec.soc.rds.dc.constant.Constant;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class BaseResult implements Serializable {
    private static final long serialVersionUID = -5771016784021901099L;

    protected String message;

    protected Integer code;

    public boolean successful() {
        return !this.failed();
    }

    public boolean failed() {
        return !Constant.SUCCESS.equals(code);
    }
}
