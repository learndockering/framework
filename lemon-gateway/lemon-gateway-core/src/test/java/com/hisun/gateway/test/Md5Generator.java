package com.hisun.gateway.test;

import com.hisun.lemon.common.security.Digests;
import com.hisun.lemon.common.utils.Encodes;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * testing
 * 
 * @author yuzhou
 * @date 2017年8月15日
 * @time 下午12:45:54
 *
 */
public class Md5Generator {
    public static void main(String[] args) {
        
        JudgeUtils.callbackIfNecessary(true, () -> {System.out.println("aaa");});
        
        ///String secure = "UhMFLPErFHAYucJI";
        String secure ="YhMFLPErFHAYucJI";
       // String param = "name=yuzhou&pageSize=2&pageNum=5";
       //String  param ="{\"body\":{\"loginId\":\"1\",\"loginPwd\":\"111111\",\"userId\":\"1\"}}";
        //String  param ="{\"loginName\":\"+86-13874815109\",\"loginPwd\":\"q1234@\"}";
        //String  param ="{\"loginName\":\"13874815109\",\"loginPwd\":\"q1234@\",\"random\":\"123\"}";
        String  param ="{\"refreshToken\":\"b100ede0-6a35-4e90-ab9c-5cbd2e0779c7\"}";
        //String  param ="{\"name\":\"yz\",\"age\":31,\"sex\":\"男\",\"birthday\":\"1985-11-11\"}";
        ///String param = "{\"body\":{\"loginId\":\"1\",\"newPwd\":\"111111\",\"oldPwd\":\"111111\",\"userId\":\"1\"}}";
        String md5 = Encodes.encodeHex(Digests.md5((param+secure).getBytes()));
        System.out.println(md5);
    }
}
