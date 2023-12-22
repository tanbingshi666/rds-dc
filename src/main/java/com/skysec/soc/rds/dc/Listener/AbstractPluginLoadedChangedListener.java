package com.skysec.soc.rds.dc.Listener;

import com.skysec.soc.rds.dc.event.plugin.PluginLoadChangedEvent;
import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public abstract class AbstractPluginLoadedChangedListener implements ApplicationListener<PluginLoadChangedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPluginLoadedChangedListener.class);

    protected abstract void add(SqlQueryObject sqlQueryObject);

    protected abstract void modify(SqlQueryObject sqlQueryObject);

    protected abstract void remove(SqlQueryObject sqlQueryObject);

    @Override
    public void onApplicationEvent(PluginLoadChangedEvent event) {
        switch (event.getPluginEventEnum()) {
            case ADD:
                this.add(event.getSqlQueryObject());
                break;
            case EDIT:
                this.modify(event.getSqlQueryObject());
                break;
            case DELETE:
                this.remove(event.getSqlQueryObject());
                break;
            default:
                LOGGER.error("插件事件类型未知 {}", event);
        }
    }

}
