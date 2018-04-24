package com.hisun.lemon.demo3.controller;

import io.swagger.annotations.Api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hisun.channel.client.IChannelClient;
import com.hisun.channel.cmsb.PayReq;
import com.hisun.channel.data.Request;
import com.hisun.channel.data.Response;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.demo3.common.TestSetting;
import com.hisun.lemon.framework.cumulative.Cumulative;
import com.hisun.lemon.framework.cumulative.Cumulative.Dimension;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.NoBody;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.framework.utils.RandomTemplete;
import com.hisun.lemon.framework.utils.RandomTemplete.RandomType;
import com.hisun.lemon.prd.client.UserClient;
import com.hisun.lemon.prd.dto.UserDTO;
import com.hisun.lemon.prd.dto.UserQueryDTO;
import com.hisun.lemon.prd.dto.UserQueryDTO.User;

/**
 * 测试fegin 调用
 * @author yuzhou
 * @date 2017年6月27日
 * @time 下午3:09:41
 *
 */
@RestController
@RequestMapping("/test")
@Api(tags="测试交易")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Resource
    private UserClient userClient;
    
    @Autowired
    private Cumulative cumulative;
    
    @Autowired
    private IChannelClient channelClient;
    
    @Autowired
    private TestSetting testSetting;
    
    @Autowired
    private RandomTemplete randomTemplete;
    
    @GetMapping("/addUser")
    public GenericRspDTO<NoBody> addUser() {
        System.out.println("---============="+LemonUtils.getEnv());
        UserDTO userDTO = new UserDTO();
        userDTO.setName("yuzhou");
        userDTO.setAge(31);
        userDTO.setBirthday(DateTimeUtils.parseLocalDate("19851010"));
        userDTO.setSex("男");
        GenericRspDTO<NoBody> dto =  this.userClient.addUser(userDTO);
        return dto;
    }
    
    @GetMapping("/findUser")
    public GenericRspDTO<?> findUser() {
        UserQueryDTO userQueryDTO = new UserQueryDTO();
       // userQueryDTO.setName("yuzhou");
        userQueryDTO.setPageNum(2);
        userQueryDTO.setPageSize(4);
        GenericDTO<UserQueryDTO> queryUser = new GenericDTO<>();
        queryUser.setBody(userQueryDTO);
        GenericRspDTO<List<User>> dto = this.userClient.findUser(queryUser);
        //判断交易是否成功
        if(JudgeUtils.isSuccess(dto.getMsgCd())) {
            System.out.println("successful....");
        }
        return dto;
    }
    
    @GetMapping("/testException")
    public GenericRspDTO<NoBody> testException() {
        return this.userClient.testException(GenericRspDTO.newSuccessInstance());
    }
    
    @GetMapping("/testCumulative/{mode}")
    public GenericRspDTO<NoBody> testCumulative(@PathVariable String mode) {
        if(JudgeUtils.equals(mode, "0")) {
            this.cumulative.countByDay("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        } else if (JudgeUtils.equals(mode, "1")) {
            this.cumulative.countByMonth("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        } else if(JudgeUtils.equals(mode, "2")) {
            this.cumulative.countByDayAndMonth("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        }
        return GenericRspDTO.newSuccessInstance();
    }
    
    @GetMapping("/queryCumulative/{mode}/{dimension}")
    public GenericRspDTO<String> testCumulative(@PathVariable String mode, @PathVariable String dimension) {
        String rst = "";
        if(JudgeUtils.equals(mode, "0")) {
           rst = this.cumulative.queryByDay("TEST", dimension);
        } else if (JudgeUtils.equals(mode, "1")) {
           rst = this.cumulative.queryByMonth("TEST", dimension);
        }
        return GenericRspDTO.newSuccessInstance(rst);
    }
    
    @GetMapping("/testChannel")
    public GenericRspDTO<?> testChannel(GenericDTO<NoBody> genericDTO) {
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
        PayReq.TranReq tranReq = new PayReq.TranReq();
        tranReq.setMchntCd("MCH0000000000001");
        tranReq.setTranDate(localDate.format(formatterDate));
        tranReq.setTranTime(localDate.format(formatterTime));
        tranReq.setTranId("1602061010000000");
        tranReq.setCurrency("RMB");
        tranReq.setPayAcctNo("PAYACCNO0000002");
        tranReq.setPayAccName("yuzhou");
        tranReq.setPayBankType("1");
        tranReq.setPayBankName("中国银行");
        tranReq.setAccNo("6226192914678983");
        tranReq.setAccName("YUZHOU");
        tranReq.setBankType("105100000017");
        tranReq.setBankName("招行");
        tranReq.setTransAmt(new BigDecimal("99.99"));
        tranReq.setRemarkCd("90804");
        tranReq.setRemark("case115");
        tranReq.setResv("case115");
        
        PayReq payReq = new PayReq();
        payReq.setCooperationCd("1000000000001");
        payReq.setTxCd("3002");
        payReq.setTranReq(tranReq);
        
        Request request = new Request();
        request.setRoute("CMSB");
        request.setBusiType("pay");
        request.setSource("tst");
        request.setTarget(payReq);
        request.setRequestId(genericDTO.getMsgId());
        Response response = channelClient.request(request);
        logger.info("channel rsp code {}", response.getMsgCode());
        logger.info("channel rsp info {}", response.getMsgInfo());
        logger.info("channel rsp object {}", response.getResult());
        return GenericRspDTO.newInstance(response.getMsgCode(), response.getResult());
    }
    
    @GetMapping("/testRandomTemplete")
    public GenericRspDTO<NoBody> testRandomTemplete() {
        String random = randomTemplete.apply("Test", 30*60*1000, RandomType.NUMERIC_LETTER, 15);
        logger.info("random is {}", random);
        randomTemplete.validateOnce("Test", random);
        return GenericRspDTO.newSuccessInstance();
    }
}
