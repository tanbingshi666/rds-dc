package com.skysec.soc.rds.dc.service.impl;

import com.skysec.soc.rds.dc.pojo.model.Config;
import org.springframework.stereotype.Service;

@Service
public class ConfigLocatingService extends AbstractConfigLocatingService<Config> {

    @Override
    public void cache(Config config) {
        configList.put(config.getId(), config);
    }

}
