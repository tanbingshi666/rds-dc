package com.skysec.soc.rds.dc.event.plugin;

import org.springframework.context.ApplicationEvent;

public class PluginBaseEvent extends ApplicationEvent {

    protected final String id;

    public PluginBaseEvent(Object source, String id) {
        super(source);
        this.id = id;
    }

}
