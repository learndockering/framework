package com.hisun.lemon.stream.test;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericCmdDTO;
import com.hisun.lemon.framework.stream.MessageHandler;

//@EnableBinding(value = {TestInOutChannel.class})
public class TestReceiver {
    private static final Logger logger = LoggerFactory.getLogger(TestReceiver.class);
    
    @SuppressWarnings("rawtypes")
    private Map<String, MessageHandler> beans = null;
    
    @StreamListener(TestInOutChannel.HELLO)
    public <T> void receive(GenericCmdDTO<T> genericCmdDTO) {
        logger.info("###Receive msg :"+ genericCmdDTO);
       // T obj = genericCmdDTO.getBody();
      //  logger.info("Nest Obj =="+obj);
        MessageHandler<T> handler = this.getMessageHandler(genericCmdDTO.getBeanName());
        handler.onMessageReceive(genericCmdDTO);
    }
    
    @SuppressWarnings("unchecked")
    private <T> MessageHandler<T> getMessageHandler(String beanName) {
        if(JudgeUtils.isNull(beans)) {
            beans = ExtensionLoader.getSpringBeansOfType(MessageHandler.class);
        }
        return beans.get(beanName);
    }
    
}
