package com.skysec.soc.rds.dc.Listener;

import com.skysec.soc.rds.dc.event.plugin.PluginLoadChangedEvent;
import com.skysec.soc.rds.dc.pojo.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public abstract class AbstractPluginLoadedChangedListener implements ApplicationListener<PluginLoadChangedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPluginLoadedChangedListener.class);

    protected abstract void add(Config config);

    protected abstract void modify(Config config);

    protected abstract void remove(Config config);

    @Override
    public void onApplicationEvent(PluginLoadChangedEvent event) {
        switch (event.getPluginEventEnum()) {
            case ADD:
                this.add(event.getConfig());
                break;
            case EDIT:
                this.modify(event.getConfig());
                break;
            case DELETE:
                this.remove(event.getConfig());
                break;
            default:
                LOGGER.error("插件事件类型未知 {}", event);
        }
    }

}
