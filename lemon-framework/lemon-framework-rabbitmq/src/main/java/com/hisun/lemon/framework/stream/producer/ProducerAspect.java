package com.hisun.lemon.framework.stream.producer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericCmdDTO;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;

/**
 * 生产者
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午7:23:36
 *
 */
@Configuration
@Aspect
public class ProducerAspect implements Ordered, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(ProducerAspect.class);
    
    private ApplicationContext applicationContext;
    private Map<String, MessageChannel> senders = new ConcurrentHashMap<>();
    
    @Around("@annotation(producers)")
    public void producer(ProceedingJoinPoint pjp, Producers producers) {
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e1) {
            LemonException.throwLemonException(e1);
        }
        
        try {
            final Object execRst = result;
            Producer[] producerArray = producers.value();
            Stream.of(producerArray).parallel().forEach(p -> {
                try{
                    sendMsg(execRst, p);
                }catch(Throwable e2) {
                    if(logger.isErrorEnabled()) {
                        logger.error("faild to sending msg to {} ==>> {}", p.channelName(), execRst);
                        logger.error("", e2);
                    }
                }
            });
        } catch (Throwable e) {         //防御性异常外抛
            if(logger.isErrorEnabled()) {
                logger.error("", e);
            }
        }
        
    }
    
    private void sendMsg(Object result, Producer producer) {
        MessageChannel sender = this.senders.get(producer.channelName());
        if(JudgeUtils.isNull(sender)) {
            LemonException.throwLemonException(ErrorMsgCode.PRODUCER_RABBIT_EXCEPTION.getMsgCd(), "MessageChannel \""+producer.channelName()+"\" does not exists.");
        }
        GenericCmdDTO<?> dto = GenericCmdDTO.newCmdInstance(producer.beanName(), result);
        LemonData lemonData = LemonHolder.getLemonData();
        if(JudgeUtils.isNotNull(lemonData)) {
            DataHelper.copyLemonDataToGenericDTO(lemonData, dto);
        }
        sender.send(MessageBuilder.withPayload(dto).build());
        if(logger.isInfoEnabled()) {
            logger.info("Send msg to {} : {}", producer.channelName(), dto);
        }
    }
    
    @PostConstruct
    private void init() {
        ExtensionLoader.getSpringBeansOfType(applicationContext, MessageChannel.class).forEach((k, v) -> {
            this.senders.put(k, v);
        });
        if(logger.isInfoEnabled()) {
            logger.info("find message channel {}", this.senders);
        }
    }
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE-2;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

}
