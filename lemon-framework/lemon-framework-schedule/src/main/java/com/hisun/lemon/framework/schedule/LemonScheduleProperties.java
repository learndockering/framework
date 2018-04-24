package com.hisun.lemon.framework.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午7:39:05
 *
 */
@ConfigurationProperties("lemon.schedule")
public class LemonScheduleProperties {
    
    private ThreadPoolProperties threadPool;
    
    public ThreadPoolProperties getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolProperties threadPool) {
        this.threadPool = threadPool;
    }



    public static class ThreadPoolProperties {
        private Integer poolSize;
        private Integer awaitTerminaltionSeconds;
        private Boolean waitForTasksToCompleteOnShutdown;
        public Integer getPoolSize() {
            return poolSize;
        }
        public void setPoolSize(Integer poolSize) {
            this.poolSize = poolSize;
        }
        public Integer getAwaitTerminaltionSeconds() {
            return awaitTerminaltionSeconds;
        }
        public void setAwaitTerminaltionSeconds(Integer awaitTerminaltionSeconds) {
            this.awaitTerminaltionSeconds = awaitTerminaltionSeconds;
        }
        public Boolean getWaitForTasksToCompleteOnShutdown() {
            return waitForTasksToCompleteOnShutdown;
        }
        public void setWaitForTasksToCompleteOnShutdown(
                Boolean waitForTasksToCompleteOnShutdown) {
            this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        }
        
    }
}
