package com.skysec.soc.rds.dc.Listener;

import com.skysec.soc.rds.dc.pojo.model.sql.SqlQueryObject;
import com.skysec.soc.rds.dc.service.SqlLocating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PluginLoadedChangedListener extends AbstractPluginLoadedChangedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoadedChangedListener.class);

    final SqlLocating<SqlQueryObject> sqlLocating;

    public PluginLoadedChangedListener(SqlLocating<SqlQueryObject> sqlLocating) {
        this.sqlLocating = sqlLocating;
    }

    @Override
    protected void add(SqlQueryObject sqlQueryObject) {
        sqlLocating.cache(sqlQueryObject);
    }

    @Override
    protected void modify(SqlQueryObject sqlQueryObject) {
    }

    @Override
    protected void remove(SqlQueryObject sqlQueryObject) {
    }
}
