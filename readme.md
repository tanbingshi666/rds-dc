# 一 概述

​	soc-rds-dc  (SOC Relation DataBase Service Data Connection) 项目意图统一 SQL 查询组件数据，目前支持 PostgresSQL，后续可以根据业务需求扩展，比如支持 ES DSL、MySQL、Doris 等

# 二 架构

​	目前基于 Nacos 作为配置中心，可插拔方式支持其他的配置中心，比如 Consul、Zookeeper、MySQL、Redis 等 (需要开发)

# 三 案例

## 3.1 配置 Nacos

​	需要指定 Nacos 的名称空间，服务会定时查询名称空间下的所有配置并缓存，目前配置文件支持 JSON 格式，

最终的基于 ${GROUP}/${DATAID} 作为 Key，查询到对应的配置，比如如下：

![](images\image-20231221112619166.png)

![image-20231221112828108](images\image-20231221112828108.png)

具体的配置参考源码类：com.skysec.soc.rds.dc.pojo.model.sql.Config

```java
public class SqlQueryObject {
    private String id;
    private String dataSource;
    private String querySql;
    private boolean isPageQuery;
    private boolean isAsync;
}
```

## 3.2 配置服务

​	参考 application.yml 文件

## 3.3 发送请求

​	目前只支持 POST 请求，比如如下：

![image-20231221113323439](images\image-20231221113323439.png)