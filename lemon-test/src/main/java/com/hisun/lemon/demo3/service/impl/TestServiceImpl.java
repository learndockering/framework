package com.hisun.lemon.demo3.service.impl;

import org.springframework.stereotype.Service;

import com.hisun.lemon.demo3.service.ITestService;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.lock.Locked;
import com.hisun.lemon.framework.utils.LemonUtils;

@Service
public class TestServiceImpl implements ITestService {

    @Override
    @Locked(lockName = "testLock", leaseTime=40, waitTime=10)
 //   @Scheduled(fixedRate=5000)
    public void testLock() {
        String str = "testLock1234567890..";
        for(char c : str.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("");
    }
    
    @Locked(lockName = "testLock", leaseTime=40, waitTime=10)
  //  @Scheduled(fixedRate=5000)
    public void testLock2() {
        String str = "testLock1234567890..";
        for(char c : str.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("");
    }
    
    //@Locked(lockName = "testLock", leaseTime=40, waitTime=10)
    //@Scheduled(fixedRate=5000)
    public void testLock3() {
        String str = "333333333333333333333..";
        for(char c : str.toCharArray()) {
            System.out.print(c+"-"+Thread.currentThread().getName()+"#");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("");
    }
    
   // @BatchScheduled(fixedRate=10000)
    public void schedule() {
        LemonData lemonData = LemonHolder.getLemonData();
        String msgId = LemonUtils.getMsgId();
        String requestId = LemonUtils.getRequestId();
        System.out.println("@BatchScheduled test.................."+lemonData);
        System.out.println(msgId);
        System.out.println(requestId);
    }

}
