package com.skysec.soc.rds.dc.plugin.nacos;

import cn.hutool.json.JSONUtil;
import com.skysec.soc.rds.dc.event.plugin.PluginEventEnum;
import com.skysec.soc.rds.dc.event.plugin.PluginLoadChangedEvent;
import com.skysec.soc.rds.dc.pojo.model.Config;
import com.skysec.soc.rds.dc.utils.FutureUtil;
import com.skysec.soc.rds.dc.utils.SpringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "scan.sql.plugin.name", havingValue = "nacos")
public class NacosPlugin {

    private static final String NACOS_REQUEST_NAMESPACE_TEMPLATE_URL = "http://%s/nacos/v2/cs/history/configs?namespaceId=%s";
    private static final String NACOS_REQUEST_GROUP_DATAID_TEMPLATE_URL = "http://%s/nacos/v2/cs/config?namespaceId=%s&group=%s&dataId=%s";

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosPlugin.class);

    private final RestTemplate restTemplate;

    @Value("${scan.sql.schedule.interval:30}")
    private long scheduleInterval;

    @Value("${nacos.address:localhost:8848}")
    private String nacosAddress;

    @Value("${nacos.namespace.id:public}")
    private String namespaceId;

    public NacosPlugin(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("使用 Nacos 插件定时去扫描 SQL 配置......");
        FutureUtil.executorFuture.scheduleTask(this::scheduleTask, 1, scheduleInterval, TimeUnit.SECONDS);
    }

    @SuppressWarnings(value = "unchecked")
    private void scheduleTask() {
        try {
            LinkedHashMap response = restTemplate.getForObject(String.format(NACOS_REQUEST_NAMESPACE_TEMPLATE_URL, nacosAddress, namespaceId), LinkedHashMap.class);
            if (response != null && response.get("code").equals(0) && response.get("message").equals("success")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                data.forEach(map -> {
                    String group = (String) map.getOrDefault("group", "");
                    String dataId = (String) map.getOrDefault("dataId", "");
                    LinkedHashMap responseGroupData = restTemplate.getForObject(String.format(NACOS_REQUEST_GROUP_DATAID_TEMPLATE_URL, nacosAddress, namespaceId, group, dataId), LinkedHashMap.class);
                    if (responseGroupData != null && responseGroupData.get("code").equals(0) && responseGroupData.get("message").equals("success")) {
                        // todo 可以判断上一次缓存配置确定发生哪种变化 这里暂时 upsert
                        String targetData = (String) responseGroupData.get("data");
                        Config config = JSONUtil.toBean(targetData, Config.class);
                        config.setId(group + "/" + dataId);
                        SpringTool.publish(new PluginLoadChangedEvent(this, group + "/" + dataId, config, PluginEventEnum.ADD));
                    } else {
                        LOGGER.info("向 Nacos 请求名称空间 {} group = {} dataid = {} 获取配置信息错误, {}", namespaceId, group, dataId, responseGroupData);
                    }
                });
            } else {
                LOGGER.info("向 Nacos 请求名称空间 {} 获取所有配置信息错误, {}", namespaceId, response);
            }
        } catch (Exception e) {
            LOGGER.error("向 Nacos 发送 HTTP 请求获取配置信息失败 " + e);
        }
    }
}
