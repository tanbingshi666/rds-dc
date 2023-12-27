package com.skysec.soc.rds.dc.event.plugin;

import com.skysec.soc.rds.dc.pojo.model.Config;
import lombok.Getter;

@Getter
public class PluginLoadChangedEvent extends PluginBaseEvent {

    private final Config config;
    private final PluginEventEnum pluginEventEnum;

    public PluginLoadChangedEvent(Object source, String id,
                                  Config config, PluginEventEnum pluginEventEnum) {
        super(source, id);
        this.config = config;
        this.pluginEventEnum = pluginEventEnum;
    }

}
