# 一 测试 PG

## 1.1 测试普通语句

Nacos 配置如下：

```json
{
    "dataSource": "PostgresSQL",
    "config":"select * from t_siem_alarm_policy_whole where alarm_level = :alarm_level"
}
```

HTTP 请求如下：

```http
POST 请求
http://127.0.0.1:8081/query/rds/sqlQuery
{
    "sqlPath": "DEFAULT_GROUP/postgresql_demo",
    "params": {
        "alarm_level": 3
    }
}
```

## 1.2 测试聚合语句

Nacos 配置如下：

```json
{
    "dataSource": "PostgresSQL",
    "config":"select alarm_level, count(1) AS cnt from t_siem_alarm_policy_whole group by alarm_level"
}
```

HTTP 请求如下：

```http
POST 请求
http://127.0.0.1:8081/query/rds/sqlQuery
{
    "sqlPath": "DEFAULT_GROUP/postgressql_agg"
}
```

## 1.3 测试分页语句

Nacos 配置如下：

```json
{
    "dataSource": "PostgresSQL",
    "config":"select * from t_siem_alarm_policy_whole where alarm_level = :alarm_level LIMIT :pageSize OFFSET :pageNo",
    "isPage": true
}
```

HTTP 请求如下：

```http
POST 请求
http://127.0.0.1:8081/query/rds/sqlQuery
{
    "sqlPath": "DEFAULT_GROUP/postgressql_page",
    "params": {
        "alarm_level": 3
    },
    "page": {
        "pageNo": 0,
        "pageSize": 10
    }
}
```

# 二 测试 ES

存在问题：是否可以将 config 统一为 JSON，但是这会导致注入参数可能出错，比如注入参数为数字、布尔等，有待改进

## 2.1 测试普通语句

Nacos 配置如下：

```json
{
 "dataSource": "ElasticSearch",
 "config": "{\n    \"query\": {\n        \"range\": {\n            \"storagetime\": {\n                \"gte\": #{startTime},\n                \"lte\": #{endTime}\n            }\n        }\n    }\n}"
}
```

HTTP 请求如下：

```http
POST 请求
http://127.0.0.1:8081/query/rds/dslQuery
{
    "dslPath": "DEFAULT_GROUP/sql_es_demo",
    "index": "event",
    "params": {
        "startTime": 1698742172454,
        "endTime": 1698742182454
    }
}
```