package com.skysec.soc.rds.dc.datasource.postgres;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ElasticSearchDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchDataSource.class);

    private final RestClient restClient;

    public ElasticSearchDataSource(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Map<String, Object>> execute(String dsl, Map<String, Object> params) {
        return null;
    }

    public List<JSONObject> listIndex() {
        String endpoint = "/_cat/indices?format=json";
        Request request = new Request("GET", endpoint);
        try {
            Response response = restClient.performRequest(request);
            if (response == null) {
                LOGGER.info(String.format("请求 %s 获取 ES 所有 Index 信息返回 null", endpoint));
                return null;
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String entity = EntityUtils.toString(response.getEntity());
                return JSONUtil.toList(entity, JSONObject.class);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("连接 ES 出错... %s", e));
        }
        return null;
    }

    public JSONObject getIndexMapping(String index) {

        JSONObject indexMappingJson = new JSONObject();

        String endpoint = String.format("/%s/_mappings", index);
        Request request = new Request("GET", endpoint);

        try {
            Response response = restClient.performRequest(request);
            if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGGER.info(String.format("请求 %s 获取 ES 所有 Index 信息返回 null", endpoint));
                return null;
            }

            String entity = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONUtil.parseObj(entity);
            JSONObject indexJson = jsonObject.getJSONObject(index);
            JSONObject mappingsJson = indexJson.getJSONObject("mappings");
            JSONObject propertiesJson = mappingsJson.getJSONObject("properties");
            if (propertiesJson != null) {
                for (Map.Entry<String, Object> entry : propertiesJson) {
                    indexMappingJson.set(entry.getKey(), JSONUtil.toJsonStr(entry.getValue()));
                }

                return indexMappingJson;
            }
        } catch (IOException e) {
            LOGGER.error(String.format("连接 ES 出错... %s", e));
        }
        return null;
    }

}
