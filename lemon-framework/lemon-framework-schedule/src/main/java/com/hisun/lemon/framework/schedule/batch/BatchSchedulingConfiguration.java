package com.hisun.lemon.framework.schedule.batch;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import com.hisun.lemon.common.condition.EnabledBatchConditional;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BatchSchedulingConfiguration{
    public static final String BATCH_SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalBatchScheduledAnnotationProcessor";
    
    @Bean(name = BatchSchedulingConfiguration.BATCH_SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Conditional(EnabledBatchConditional.class)
    public BatchScheduledAnnotationBeanPostProcessor batchScheduledAnnotationProcessor() {
        return new BatchScheduledAnnotationBeanPostProcessor();
    }
   
}
