package com.skysec.soc.rds.dc.datasource.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Properties;

// todo 是否开启动态多数据源
// @Configuration
public class DynamicDateSourceConfig implements EnvironmentAware, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Environment environment;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProviderSupport() {
        return new DynamicDataSourceProviderSupport();
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        return new DynamicDataSource(dynamicDataSourceProvider);
    }

    @Bean
    public NamedParameterJdbcTemplate dynamicNamedParameterJdbcTemplate(DynamicDataSource dynamicDataSource) {
        return new NamedParameterJdbcTemplate(dynamicDataSource);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // todo 注册多数据源 (这里举个例子 可以基于 environment 或者其他客户端 (ConsulClient、HTTPClient、JDBC 等一切可以获取配置信息)
        // todo 获取多数据源配置信息并注册数据源 如果有动态数据源加载要求 可以在 DynamicDataSource 注册)
        registryPostgresSQLDataSource(beanDefinitionRegistry);
        registryMySQLDataSource(beanDefinitionRegistry);
    }

    private void registryPostgresSQLDataSource(BeanDefinitionRegistry beanDefinitionRegistry) {
        String driverClassName = environment.getProperty("postgres.driverClassName");
        String url = environment.getProperty("postgres.url");
        String username = environment.getProperty("postgres.username");
        String password = environment.getProperty("postgres.password");
        Properties properties = new Properties();
        properties.setProperty("driverClassName", driverClassName);
        properties.setProperty("url", url);
        properties.setProperty("username", username);
        properties.setProperty("password", password);

        BeanDefinitionBuilder dataSourceBuilder = initDataSourceBean(properties);
        beanDefinitionRegistry.registerBeanDefinition(DynamicDataSourceEnum.POSTGRESSQL.getDataSourceName(), dataSourceBuilder.getBeanDefinition());
    }

    private void registryMySQLDataSource(BeanDefinitionRegistry beanDefinitionRegistry) {
        String driverClassName = environment.getProperty("mysql.driverClassName");
        String url = environment.getProperty("mysql.url");
        String username = environment.getProperty("mysql.username");
        String password = environment.getProperty("mysql.password");
        Properties properties = new Properties();
        properties.setProperty("driverClassName", driverClassName);
        properties.setProperty("url", url);
        properties.setProperty("username", username);
        properties.setProperty("password", password);

        BeanDefinitionBuilder dataSourceBuilder = initDataSourceBean(properties);
        beanDefinitionRegistry.registerBeanDefinition(DynamicDataSourceEnum.MYSQL.getDataSourceName(), dataSourceBuilder.getBeanDefinition());
    }

    private BeanDefinitionBuilder initDataSourceBean(Properties properties) {
        BeanDefinitionBuilder dataSourceBuilder = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource.class);
        dataSourceBuilder.setLazyInit(false);
        dataSourceBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        dataSourceBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
        dataSourceBuilder.addPropertyValue("driverClassName", properties.getProperty("driverClassName"));
        dataSourceBuilder.addPropertyValue("url", properties.getProperty("url"));
        dataSourceBuilder.addPropertyValue("username", properties.getProperty("username"));
        dataSourceBuilder.addPropertyValue("password", properties.getProperty("password"));

        return dataSourceBuilder;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // nothing to do
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
