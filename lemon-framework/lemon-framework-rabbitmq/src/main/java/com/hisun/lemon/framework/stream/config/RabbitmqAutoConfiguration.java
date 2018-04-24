package com.hisun.lemon.framework.stream.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hisun.lemon.framework.stream.MultiOutput;
import com.hisun.lemon.framework.stream.consumer.DefaultInputConsumer;
import com.hisun.lemon.framework.stream.producer.ProducerAspect;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 上午10:56:39
 *
 */
@Configuration
@Import({DefaultInputConsumer.class, MultiOutput.class, ProducerAspect.class})
public class RabbitmqAutoConfiguration {

}
