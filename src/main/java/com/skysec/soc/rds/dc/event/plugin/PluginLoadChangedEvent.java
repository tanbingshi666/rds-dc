package com.skysec.soc.rds.dc.event.plugin;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import lombok.Getter;

@Getter
public class PluginLoadChangedEvent extends PluginBaseEvent {

    private final SqlQueryObject sqlQueryObject;
    private final PluginEventEnum pluginEventEnum;

    public PluginLoadChangedEvent(Object source, String id,
                                  SqlQueryObject sqlQueryObject, PluginEventEnum pluginEventEnum) {
        super(source, id);
        this.sqlQueryObject = sqlQueryObject;
        this.pluginEventEnum = pluginEventEnum;
    }

}
