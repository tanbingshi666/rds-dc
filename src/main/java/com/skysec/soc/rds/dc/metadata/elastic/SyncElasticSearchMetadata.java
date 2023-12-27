package com.skysec.soc.rds.dc.metadata.elastic;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.skysec.soc.rds.dc.datasource.postgres.ElasticSearchDataSource;
import com.skysec.soc.rds.dc.metadata.SyncMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class SyncElasticSearchMetadata implements SyncMetadata {

    private final ElasticSearchDataSource elasticSearchDataSource;

    public SyncElasticSearchMetadata(ElasticSearchDataSource elasticSearchDataSource) {
        this.elasticSearchDataSource = elasticSearchDataSource;
    }

    @Override
    public void doSyncMetadata() {

        final List<JSONObject> indexList = elasticSearchDataSource.listIndex();
        if (CollectionUtils.isEmpty(indexList)) return;

        for (JSONObject indexJson : indexList) {
            String index = indexJson.getStr("index");

            JSONObject tableMetadataResult = new JSONObject();
            tableMetadataResult.set("table_source", "ElasticSearch");
            tableMetadataResult.set("table_catalog", "");
            tableMetadataResult.set("table_schema", "");
            tableMetadataResult.set("table_name", index);
            tableMetadataResult.set("table_rows", indexJson.getStr("docs.count"));
            tableMetadataResult.set("table_size", indexJson.getStr("pri.store.size"));
            tableMetadataResult.set("table_comment", "");
            tableMetadataResult.set("table_version", System.currentTimeMillis());

            JSONObject indexMappingJson = elasticSearchDataSource.getIndexMapping(index);
            if (indexMappingJson != null) {
                tableMetadataResult.putAll(indexMappingJson);
            }

            // todo 如何存储 PG 的元数据信息 ? 直接使用 ES 还是其他框架 比如图数据库 HugeGraph (百度开源, 推荐使用并统一管理平台的元数据以及后续规划的数据血缘)
            // todo ES 方式一: index = metadata_${tableName}, data = tableMetadataResult (推荐使用)
            // todo ES 方式二: index = metadata, data = tableMetadataResult (元数据字段放在一个 mapping 里面)
            // todo 图数据库 HugeGraph: 待定
            System.out.println(JSONUtil.toJsonPrettyStr(tableMetadataResult));
        }

    }
}
