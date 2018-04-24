package com.hisun.lemon.framework.schedule;

import static com.hisun.lemon.common.utils.NumberUtils.getDefaultIfNull;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

/**
 * schedule configuration
 * 
 * @author yuzhou
 * @date 2017年7月6日
 * @time 上午10:06:44
 *
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer{
    private static final Logger logger = LoggerFactory.getLogger(SchedulingConfiguration.class);
    
    private static final int DEFAULT_POOLSIZE = 10;
    private static final int DEFAULT_AWAIT_TERMINALTION_SECONDS = 30;
    private static final boolean DEFAULT_WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN = true;
    
    private static final String TASK_THREAD_NAME_PREFIX = "lemon-task-";
    
    @Configuration
    @EnableConfigurationProperties(LemonScheduleProperties.class)
    public static class ScheduleThreadPool {
        
        private LemonScheduleProperties lemonScheduleProperties;
        
        public ScheduleThreadPool(LemonScheduleProperties lemonScheduleProperties) {
            this.lemonScheduleProperties = lemonScheduleProperties;
        }
        
        @Bean
        public ThreadPoolTaskScheduler taskScheduler() {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(getDefaultIfNull(this.lemonScheduleProperties.getThreadPool().getPoolSize(), DEFAULT_POOLSIZE));
            scheduler.setErrorHandler(new ErrorHandler(){
                @Override
                public void handleError(Throwable t) {
                    if(logger.isErrorEnabled()) {
                        logger.error("Unexpected error occured in scheduled task.", t);
                    }
                    if(t instanceof StopSchedulingTaskException) {
                        if(logger.isErrorEnabled()) {
                            logger.error("Stop scheduling task because of occuring \"StopSchedulingTaskException\" in task.");
                        }
                        throw (StopSchedulingTaskException) t;
                    }
                }
            });
            scheduler.setThreadNamePrefix(TASK_THREAD_NAME_PREFIX);
            scheduler.setAwaitTerminationSeconds(getDefaultIfNull(this.lemonScheduleProperties.getThreadPool().getAwaitTerminaltionSeconds(), DEFAULT_AWAIT_TERMINALTION_SECONDS));
            scheduler.setWaitForTasksToCompleteOnShutdown(Optional.ofNullable(this.lemonScheduleProperties.getThreadPool().getWaitForTasksToCompleteOnShutdown()).orElse(DEFAULT_WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN));
            scheduler.setRejectedExecutionHandler(new RejectedExecutionHandler(){
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    if(logger.isErrorEnabled()) {
                        logger.error("Please increase pool size \"lemon.schedule.threadPool.poolSize\"for thread pool.");
                    }
                    throw new RejectedExecutionException("Task " + r.toString() +" rejected from " + executor.toString());
                }
            });
            return scheduler;
        }

        public LemonScheduleProperties getLemonScheduleProperties() {
            return lemonScheduleProperties;
        }

        public void setLemonScheduleProperties(
                LemonScheduleProperties lemonScheduleProperties) {
            this.lemonScheduleProperties = lemonScheduleProperties;
        }
        
    }

    @Resource(name = "taskScheduler")
    private ThreadPoolTaskScheduler taskScheduler;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler);
    }
    
}
