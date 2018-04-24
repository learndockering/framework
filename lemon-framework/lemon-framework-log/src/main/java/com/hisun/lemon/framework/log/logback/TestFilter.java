package com.hisun.lemon.framework.log.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 
 * @author yuzhou
 * @date 2017年7月7日
 * @time 下午4:25:06
 *
 */
public class TestFilter extends Filter<ILoggingEvent>{

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if(event.getThreadName().startsWith("lemon-task")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("Eureka-JerseyClient-Conn-Cleaner")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("DiscoveryClient-CacheRefreshExecutor")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("DiscoveryClient-HeartbeatExecutor")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("PollingServerListUpdater-")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("NFLoadBalancer-PingTimer-")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("SimpleHostRoutingFilter.connectionManagerTimer")) {
            return FilterReply.DENY;
        }
        if(event.getThreadName().startsWith("DiscoveryClient-InstanceInfoReplicator")) {
            return FilterReply.DENY;
        }
        if("com.netflix.discovery.DiscoveryClient".equals(event.getLoggerName())) {
            return FilterReply.DENY;
        }
        
        if(event.getLoggerName().startsWith("com.netflix.discovery.shared")) {
            return FilterReply.DENY;
        }
        if("org.apache.http.headers".equals(event.getLoggerName())) {
            return FilterReply.DENY; 
        }
        if("org.apache.http.wire".equals(event.getLoggerName())) {
            return FilterReply.DENY; 
        }
        return FilterReply.ACCEPT;
    }

}
