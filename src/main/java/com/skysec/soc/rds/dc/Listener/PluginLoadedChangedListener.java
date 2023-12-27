package com.skysec.soc.rds.dc.Listener;

import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.service.ConfigLocating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PluginLoadedChangedListener extends AbstractPluginLoadedChangedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoadedChangedListener.class);

    final ConfigLocating<Config> configLocating;

    public PluginLoadedChangedListener(ConfigLocating<Config> configLocating) {
        this.configLocating = configLocating;
    }

    @Override
    protected void add(Config config) {
        configLocating.cache(config);
    }

    @Override
    protected void modify(Config config) {
    }

    @Override
    protected void remove(Config config) {
    }
}
