package com.hisun.lemon.stream.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hisun.lemon.TestApplication;
import com.hisun.lemon.framework.data.GenericCmdDTO;
import com.hisun.lemon.framework.stream.MultiOutput.DefaultSender;
import com.hisun.lemon.framework.stream.MultiOutput.OneSender;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class HelloApplicationTest {
    private static final Logger logger = LoggerFactory.getLogger(HelloApplicationTest.class);
    
    //@Autowired
    private TestInOutChannel sender;
    
   // @Autowired
    private OneSender oneSender;
    
    //@Autowired
    private DefaultSender defaultSender;
    
    @Autowired
    private TestProducer testProducer;
    
    //@Test
    public void contextLoads(){
        Hello hello = new Hello("yuzhou",23);
        GenericCmdDTO<Hello> dto = new GenericCmdDTO<>("helloMessageHandler");
        dto.setBody(hello);
        for(int i= 0 ; i< 100; i++) {
            sender.output().send(MessageBuilder.withPayload(dto).build());
            logger.info("###sended msg {}", dto);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
        
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
        }
    }
    //@Test
    public void outputTest(){
        System.out.println("oneSender==="+oneSender);
        Hello hello = new Hello("yuzhou",23);
        GenericCmdDTO<Hello> dto = new GenericCmdDTO<>("helloMessageHandler");
        dto.setBody(hello);
        for(int i= 0 ; i< 100; i++) {
            defaultSender.output().send(MessageBuilder.withPayload(dto).build());
            logger.info("###sended msg {}", dto);
            oneSender.output().send(MessageBuilder.withPayload(dto).build());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
        
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
        }
    }
    
    @Test
    public void testProducer() {
        for(int i = 0; i< 100; i++) {
            testProducer.sendHello();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
