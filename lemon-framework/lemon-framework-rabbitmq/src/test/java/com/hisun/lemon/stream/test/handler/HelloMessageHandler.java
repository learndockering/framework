package com.hisun.lemon.stream.test.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hisun.lemon.framework.data.GenericCmdDTO;
import com.hisun.lemon.framework.stream.MessageHandler;
import com.hisun.lemon.stream.test.Hello;

@Component("helloMessageHandler")
public class HelloMessageHandler implements MessageHandler<Hello> {
    private static final Logger logger = LoggerFactory.getLogger(HelloMessageHandler.class);
    
    @Override
    public void onMessageReceive(GenericCmdDTO<Hello> genericCmdDTO) {
        logger.info("Receive msg hand {}", genericCmdDTO.getBody());
    }
}
