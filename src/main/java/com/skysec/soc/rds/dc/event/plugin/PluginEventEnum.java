package com.skysec.soc.rds.dc.event.plugin;

public enum PluginEventEnum {

    UNKNOWN(0, "unknown"),
    ADD(1, "新增"),
    DELETE(2, "删除"),
    EDIT(3, "修改");

    PluginEventEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;
    private final String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PluginEventEnum valueOf(Integer code) {
        if (code == null) {
            return PluginEventEnum.UNKNOWN;
        }
        for (PluginEventEnum state : PluginEventEnum.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }

        return PluginEventEnum.UNKNOWN;
    }

    public static boolean validate(Integer code) {
        if (code == null) {
            return false;
        }

        for (PluginEventEnum state : PluginEventEnum.values()) {
            if (state.getCode() == code) {
                return true;
            }
        }

        return false;
    }
}
