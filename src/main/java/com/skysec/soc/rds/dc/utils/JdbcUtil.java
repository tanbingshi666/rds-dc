package com.skysec.soc.rds.dc.utils;

import com.skysec.soc.rds.dc.pojo.sql.SQLExecutionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtil.class);

    private JdbcUtil() {
    }

    public static SQLExecutionInfo executeQuery(DataSource dataSource, String querySQL) {

        SQLExecutionInfo result = SQLExecutionInfo.builder().build();

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(querySQL)
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                result.getColumns().add(metaData.getColumnName(i));
            }

            List<List<Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    Object data = getObject(resultSet, i);
                    row.add(data);
                }
                rows.add(row);
            }

            result.setRows(rows);
        } catch (Exception e) {
            LOGGER.error("失败执行 SQL = {}, 错误信息: {}", querySQL, e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }

    private static Object getObject(ResultSet resultSet, int i) {
        try {
            Object object = resultSet.getObject(i);
            if (object instanceof Date) {
                return resultSet.getTimestamp(i);
            }
            return object;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
