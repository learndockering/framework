package com.hisun.lemon.framework.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * 动态数据源
 * @author yuzhou
 * @date 2017年7月7日
 * @time 上午10:02:40
 *
 */
public class DynamicDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegistrar.class);

    private Environment environment;
    //默认数据源名称
    private String defaultDataSourceName = "primary";
    // 默认数据源
    private DataSource defaultDataSource;
    // 动态数据源
    private Map<String, DataSource> dynamicDataSources = new HashMap<>();

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    /* 
     * 注册动态数据源
     * @see org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(this.environment, "lemon.dynamicDataSource.");
        String enable = propertyResolver.getProperty("enabled");
        if(JudgeUtils.isBlank(enable)) {
            enable = "true";
        }
        if(! Boolean.valueOf(enable)) {
            return ;
        }
        findDefaultDataSource(importingClassMetadata);
        registerDataSources();
        registerDynamicDataSource(importingClassMetadata, registry);
    }
    
    public void findDefaultDataSource(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> defaultAttrs = importingClassMetadata.getAnnotationAttributes(EnableDynamicDataSource.class.getName(), true);
        if(defaultAttrs.containsKey("defaultDataSource")) {
            String defaultDataSource = (String) defaultAttrs.get("defaultDataSource");
            if(JudgeUtils.isNotBlank(defaultDataSource)) {
                this.defaultDataSourceName = defaultDataSource;
            }
        }
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(this.environment, "lemon.dynamicDataSource.");
        if(JudgeUtils.isNotBlank(propertyResolver.getProperty("defaultDataSource"))) {
            this.defaultDataSourceName = propertyResolver.getProperty("defaultDataSource");
        }
        if(logger.isDebugEnabled()) {
            logger.debug("default data source is {}", this.defaultDataSourceName);
        }
    }
    
    private void registerDataSources() {
        RelaxedPropertyResolver datasourcesPropertyResolver = new RelaxedPropertyResolver(this.environment, "dataSource.");
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(this.environment, "lemon.dynamicDataSource.");
        String dsPrefixs = propertyResolver.getProperty("dataSources");
        if(JudgeUtils.isBlank(dsPrefixs)) {
            throw new LemonException("dynamic data source is blank, please config the property \"lemon.dynamicDataSource.dataSources\" in configuration file.");
        }
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            Map<String, Object> map = datasourcesPropertyResolver.getSubProperties(dsPrefix + ".");
            DataSource ds = initDataSource(map);
            // 设置默认数据源
            if(JudgeUtils.equals(this.defaultDataSourceName, dsPrefix)) {
                defaultDataSource = ds;
            } else {
                dynamicDataSources.put(dsPrefix, ds);
            }
            dataBinder(ds, dsPrefix);
            if(logger.isInfoEnabled()) {
                logger.info("create data source {}",ds);
            }
        }
    }

    /**
     * 注册动态数据源
     * @param importingClassMetadata
     * @param registry
     */
    private void registerDynamicDataSource(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(this.defaultDataSourceName, defaultDataSource);
        targetDataSources.putAll(dynamicDataSources);
        
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        beanDefinition.setPrimary(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }
    /**
     * 初始化数据源
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataSource initDataSource(Map<String, Object> map) {
        String driverClassName = map.get("driverClassName").toString();
        String url = map.get("url").toString();
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String dsType = map.get("type").toString();
        Class<DataSource> dataSourceType;
        DataSource dataSource = null;
        try {
            dataSourceType = (Class<DataSource>) Class.forName(dsType);
            dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType).build();;
        } catch (ClassNotFoundException e) {
            if(logger.isErrorEnabled()) {
                logger.error("init data source error.", e);
            }
            throw new LemonException(e);
        }
        return dataSource;
    }

    /**
     * 加载数据源配置信息
     * @param dataSource
     * @param dsName
     */
    private void dataBinder(DataSource dataSource, String dsName) {
        String [] disallowedFields = new String[]{"driverClassName","url","username","password","type"};
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setIgnoreNestedProperties(false);// false
        dataBinder.setIgnoreInvalidFields(false);// false
        dataBinder.setIgnoreUnknownFields(true);// true
        dataBinder.setDisallowedFields(disallowedFields);
        Map<String, Object> values = new RelaxedPropertyResolver(this.environment, "dataSource."+dsName).getSubProperties(".");
        dataBinder.bind(new MutablePropertyValues(values));
    }

}
