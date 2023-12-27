package com.skysec.soc.rds.dc.metadata.postgres;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.skysec.soc.rds.dc.datasource.DataSourceManager;
import com.skysec.soc.rds.dc.datasource.postgres.PostgresSQLDataSource;
import com.skysec.soc.rds.dc.metadata.SyncMetadata;
import com.skysec.soc.rds.dc.pojo.sql.SQLExecutionInfo;
import com.skysec.soc.rds.dc.utils.JdbcUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SyncPostgresSQLMetadata implements SyncMetadata {

    private static final String SHOW_TABLES_SQL = "SELECT table_catalog, table_schema ,table_name  FROM information_schema.tables where table_type = 'BASE TABLE' and table_schema = 'public';";

    private static final String TABLE_METADATA_SQL = "SELECT\n" +
            "  T.schemaname as schema_name,\n" +
            "  T.tablename as table_name,\n" +
            "  C.reltuples as table_rows,\n" +
            "  pg_total_relation_size ( schemaname || '.' || tablename ) AS table_size,\n" +
            "  cast(obj_description(C.relfilenode) as varchar) AS table_comment\n" +
            "FROM\n" +
            "  pg_tables T\n" +
            "  LEFT JOIN pg_class C ON C.relname = T.tablename\n" +
            "  LEFT JOIN pg_namespace N ON C.relnamespace = N.oid\n" +
            "WHERE\n" +
            "  T.schemaname = n.nspname\n" +
            "  AND C.relkind = 'r'\n" +
            "  AND n.nspname = 'public'\n" +
            "  AND T.tablename IN ( '%s' );";

    private final static String TABLE_COLUMN_METADATA_SQL = "SELECT\n" +
            "  C.table_name,\n" +
            "  C.ordinal_position,\n" +
            "  C.column_name,\n" +
            "  C.data_type,\n" +
            "  COALESCE(C.character_maximum_length,C.numeric_precision) as data_length,\n" +
            "  C.is_nullable,\n" +
            "  col_description((C.table_schema||'.'||C.table_name)::regclass::oid, C.ordinal_position) as column_comment,\n" +
            "  itc.constraint_type\n" +
            "FROM\n" +
            "  ( SELECT * FROM information_schema.COLUMNS ic WHERE ic.table_schema = 'public' AND ic.TABLE_NAME = '%s' ) C \n" +
            "LEFT JOIN information_schema.key_column_usage iku ON C.table_schema = iku.table_schema\n" +
            "  AND C.TABLE_NAME = iku.TABLE_NAME\n" +
            "  AND C.COLUMN_NAME = iku.COLUMN_NAME\n" +
            "LEFT JOIN information_schema.table_constraints itc ON itc.CONSTRAINT_NAME = iku.CONSTRAINT_NAME\n" +
            "  AND C.table_schema = itc.table_schema\n" +
            "  AND itc.constraint_type != 'CHECK';";

    final DataSourceManager dataSourceManager;

    private DataSource dataSource;

    public SyncPostgresSQLMetadata(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    @PostConstruct
    public void init() {
        this.dataSource = dataSourceManager
                .getDataSource(PostgresSQLDataSource.PG_DATASOURCE_IDENTIFIED)
                .getDataSource();
    }

    @Override
    public void doSyncMetadata() {

        SQLExecutionInfo showTablesInfo = JdbcUtil.executeQuery(dataSource, SHOW_TABLES_SQL);

        if (showTablesInfo.getSuccess()) {
            // 如果表名存在 . 后续执行 SQL 会报错 直接过滤
            List<List<Object>> targetTableInfo = showTablesInfo.getRows()
                    .stream()
                    .filter(objects -> {
                        String tableName = (String) objects.get(2);
                        return !tableName.contains(".");
                    }).collect(Collectors.toList());
            for (List<Object> tableInfo : targetTableInfo) {
                JSONObject tableMetadataResult = new JSONObject();

                Object tableCatalog = tableInfo.get(0);
                Object tableSchema = tableInfo.get(1);
                Object tableName = tableInfo.get(2);

                tableMetadataResult.set("table_source", "PostgresSQL");
                tableMetadataResult.set("table_catalog", tableCatalog);
                tableMetadataResult.set("table_schema", tableSchema);
                tableMetadataResult.set("table_name", tableName);

                SQLExecutionInfo tableMetadataInfo = JdbcUtil.executeQuery(dataSource, String.format(TABLE_METADATA_SQL, tableName));
                if (tableMetadataInfo.getSuccess()) {
                    List<Object> tableMetadata = tableMetadataInfo.getRows().get(0);
                    tableMetadataResult.set("table_rows", tableMetadata.get(2) + "");
                    tableMetadataResult.set("table_size", tableMetadata.get(3) + "");
                    tableMetadataResult.set("table_comment", tableMetadata.get(4));
                }

                tableMetadataResult.set("table_version", System.currentTimeMillis());

                final SQLExecutionInfo tableColumnMetadataInfo = JdbcUtil.executeQuery(dataSource, String.format(TABLE_COLUMN_METADATA_SQL, tableName));
                if (tableColumnMetadataInfo.getSuccess()) {
                    for (List<Object> columnRow : tableColumnMetadataInfo.getRows()) {
                        final List<String> columns = tableColumnMetadataInfo.getColumns();
                        final JSONObject oneColumnJson = new JSONObject();
                        for (int i = 0; i < columns.size(); i++) {
                            oneColumnJson.set(columns.get(i), columnRow.get(i));
                        }
                        tableMetadataResult.set((String) columnRow.get(2), oneColumnJson.toJSONString(0));
                    }
                }

                // todo 如何存储 PG 的元数据信息 ? 直接使用 ES 还是其他框架 比如图数据库 HugeGraph (百度开源, 推荐使用并统一管理平台的元数据以及后续规划的数据血缘)
                // todo ES 方式一: index = metadata_${tableName}, data = tableMetadataResult (推荐使用)
                // todo ES 方式二: index = metadata, data = tableMetadataResult (元数据字段放在一个 mapping 里面)
                // todo 图数据库 HugeGraph: 待定
                System.out.println(JSONUtil.toJsonPrettyStr(tableMetadataResult));
            }

        }
    }
}
