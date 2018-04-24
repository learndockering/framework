package com.hisun.lemon.framework.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author yuzhou
 * @date 2017年7月6日
 * @time 下午7:53:32
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
    
    private static final ThreadLocal<String> datasourceHolder = new ThreadLocal<>();
    
    @Override
    protected Object determineCurrentLookupKey() {
        return getDatasource();
    }
    
    public static String getDatasource() {
        if(logger.isDebugEnabled()) {
            logger.debug("Acquired dataSource {}", datasourceHolder.get());
        }
        return datasourceHolder.get();
    }
    
    public static void setDatasource(String datasourceName) {
        datasourceHolder.set(datasourceName);
    }
    
    public static void clearDatasource() {
        datasourceHolder.remove();
    }

}
