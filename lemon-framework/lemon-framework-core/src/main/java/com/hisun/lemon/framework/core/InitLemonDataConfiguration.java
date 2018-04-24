package com.hisun.lemon.framework.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableInitLemonData(supportAnnotationClassNames={InitLemonDataConfiguration.BATCH_SCHEDULED, InitLemonDataConfiguration.SCHEDULED})
public class InitLemonDataConfiguration {
    public final static String BATCH_SCHEDULED = "com.hisun.lemon.framework.schedule.batch.BatchScheduled";
    public final static String SCHEDULED = "org.springframework.scheduling.annotation.Scheduled";

    @Bean
    public LemonDataInitializer webRequestLemonDataInitializer() {
        return new WebRequestLemonDataInitializer();
    }
}
