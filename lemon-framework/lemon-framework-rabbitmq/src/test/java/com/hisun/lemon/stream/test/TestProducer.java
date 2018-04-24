package com.hisun.lemon.stream.test;

import org.springframework.stereotype.Component;

import com.hisun.lemon.framework.stream.MultiOutput;
import com.hisun.lemon.framework.stream.producer.Producer;
import com.hisun.lemon.framework.stream.producer.Producers;

@Component
public class TestProducer {
    @Producers({
        @Producer(beanName="helloMessageHandler", channelName=MultiOutput.OUTPUT_DEFAULT),
        @Producer(beanName="helloMessageHandler2", channelName=MultiOutput.OUTPUT_DEFAULT)
    })
    public Hello sendHello() {
        return new Hello("hello-->",40);
    }
}
