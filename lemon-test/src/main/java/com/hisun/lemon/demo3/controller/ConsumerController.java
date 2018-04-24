package com.hisun.lemon.demo3.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisun.lemon.demo3.clientinterface.ComputeClient;
import com.hisun.lemon.framework.idgenerate.IdGenerator;
import com.hisun.lemon.framework.utils.IdGenUtils;

@RestController
@Api(tags="消费交易")
public class ConsumerController {
   // private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    
    @Autowired
    private ComputeClient computeClient;
    
    @Autowired
    private IdGenerator idGenerator;
    
    @ApiOperation(value="数字相加", notes="参数a和b相加")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "a", value = "参数a", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "b", value = "参数b", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        return computeClient.add(a, b);
    }

     @GetMapping(value = "/getId")
    public String getId(@RequestParam String key){
        //return idGenerator.generateId(key);
         //return IdGenUtils.generateId(key);
         return IdGenUtils.generateCommonId(key);
    }
     
    @GetMapping("/throwException")
    public String throwException() throws Exception {
        throw new Exception("");
    }

}
