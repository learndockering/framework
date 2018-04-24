package com.hisun.lemon.framework.stream.consumer;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.common.condition.DisabledBatchConditional;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericCmdDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.stream.DefaultInput;
import com.hisun.lemon.framework.stream.MessageHandler;

/**
 * input 通道消费者，可以配置多个主题
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午4:49:11
 *
 */
@Configuration
@ConditionalOnProperty(value=DefaultInputConsumer.ENABLE, matchIfMissing= true)
@Conditional(DisabledBatchConditional.class)
@EnableBinding(value = {DefaultInput.class})
public class DefaultInputConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInputConsumer.class);
    public static final String ENABLE = "spring.cloud.stream.bindings." + DefaultInput.DEFAULT_INPUT + ".consumer.enable";
    
    private Lock lock = new ReentrantLock();
    
    @SuppressWarnings("rawtypes")
    private Map<String, MessageHandler> beans = null;
    
    @StreamListener(DefaultInput.DEFAULT_INPUT)
    public <T> void receive(GenericCmdDTO<T> genericCmdDTO) {
        if(logger.isInfoEnabled()) {
            logger.info("Receive message from {} : {}", DefaultInput.DEFAULT_INPUT, genericCmdDTO);
        }
        if(JudgeUtils.isNull(genericCmdDTO)) {
            if(logger.isWarnEnabled()) {
                logger.warn("Received null object from default input channel.");
            }
            return;
        }
        try{
            if(JudgeUtils.isBlank(genericCmdDTO.getBeanName())) {
                if(logger.isErrorEnabled()) {
                    logger.error("The bean name is blank, Failed during handing message {}", genericCmdDTO);
                }
                return;
            }
            DataHelper.copyGenericDTOToLemonData(genericCmdDTO, LemonHolder.getLemonData());
            MessageHandler<T> handler = this.getMessageHandler(genericCmdDTO.getBeanName());
            if(JudgeUtils.isNull(handler)) {
                if(logger.isErrorEnabled()) {
                    logger.error("Handler {} does not exists. Failed during handing message {},", genericCmdDTO.getBeanName(), genericCmdDTO);
                }
                return;
            }
            handler.onMessageReceive(genericCmdDTO);
        } catch(Throwable t) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed during handing message {},", genericCmdDTO);
                logger.error("", t);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> MessageHandler<T> getMessageHandler(String beanName) {
        if(JudgeUtils.isNull(beans)) {
            try{
                lock.lock();
                if(JudgeUtils.isNull(beans)){
                    beans = ExtensionLoader.getSpringBeansOfType(MessageHandler.class);
                }
            }finally {
                lock.unlock();
            }
        }
        return beans.get(beanName);
    }
}
