package com.hisun.lemon.demo3.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.hisun.lemon.common.utils.BeanUtils;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.demo3.common.MsgCd;
import com.hisun.lemon.demo3.dto.UserDTO;
import com.hisun.lemon.demo3.dto.UserRspDTO;
import com.hisun.lemon.demo3.entity.UserDO;
import com.hisun.lemon.demo3.service.IUserService;
import com.hisun.lemon.framework.annotation.LemonBody;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.NoBody;
import com.hisun.lemon.framework.utils.PageUtils;
import com.hisun.lemon.prd.client.UserClient;
import com.hisun.lemon.prd.dto.UserQueryDTO;
import com.hisun.lemon.prd.dto.UserQueryDTO.User;

/**
 * 用户
 * @GetMapping、@PostMapping、@PutMapping、@DeleteMapping、@PatchMapping
 * @author yuzhou
 * @date 2017年6月7日
 * @time 下午2:23:30
 *
 */
@RestController
@RequestMapping("/user")
@Api(tags="用户交易")
public class UserController {
    
    /**
     * 用户服务
     */
    @Resource
    private IUserService userService;
    
    @Resource
    private UserClient userClient;
    
    @ApiOperation(value="新增用户", notes="新增用户,form表单",consumes="application/x-www-form-urlencoded", 
        produces="application/json", httpMethod="GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String",paramType="query"),
        @ApiImplicitParam(name = "sex", value = "性别", required = true, dataType = "String",paramType="query"),
        @ApiImplicitParam(name = "birthday", value = "出生日期", required = true, dataType = "Date",paramType="query"),
        @ApiImplicitParam(name = "age", value = "年龄", required = false, dataType = "Integer",paramType="query")
    })
    @ApiResponse(code = 200, message = "新增用户结果，SUCCESS 成功， FAIL 失败")
    @GetMapping("/addUser2")
    public GenericRspDTO<NoBody> addUser(@RequestParam String name, @RequestParam String sex,
            @RequestParam String birthday, @RequestParam Integer age) {
        UserDO userDO = new UserDO();
        userDO.setName(name);
        userDO.setAge(age);
        userDO.setBirthday(DateTimeUtils.parseLocalDate(birthday));
        userDO.setSex(sex);
        this.userService.addUser(userDO);
        return GenericRspDTO.newSuccessInstance();
    }
    
    @ApiOperation(value="新增用户", notes="新增用户信息", produces="application/json")
    @ApiResponse(code = 200, message = "新增用户结果", response=GenericRspDTO.class)
    @PostMapping("/addUser")
    public GenericRspDTO<NoBody> addUser(@Validated @RequestBody UserDTO userDTO) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDO, userDTO);
        this.userService.addUser(userDO);
        return GenericRspDTO.newSuccessInstance();
    }
    
    @ApiOperation(value="根据ID查找用户", notes="查找用户")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")
    @ApiResponse(code = 200, message = "用户信息")
    @GetMapping("/findUser")
    public GenericRspDTO<NoBody> findUser(@RequestParam String userId) {
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
        }*/
        UserDO userDO = this.userService.findUser(userId);
        if (JudgeUtils.isNull(userDO)){
            return GenericRspDTO.newInstance(MsgCd.USER_IS_NULL.getMsgCd());
        }
        return GenericRspDTO.newSuccessInstance(UserRspDTO.class, userDO);
    }
    
    
    @ApiOperation(value="分页查找用户", notes="分页查找用户")
    @ApiResponse(code = 200, message = "用户信息")
    @PostMapping("/findUser2")
    public GenericRspDTO<List<User>> findUser2(@Validated @RequestBody UserQueryDTO userQueryDTO) {
       // UserQueryDTO userQueryDTO = queryUserDTO.getBody();
        //查询对象
        UserDO queryUserDO = new UserDO();
        queryUserDO.setName(userQueryDTO.getName());
        //以下是分页查询
        List<UserDO> userDOs = PageUtils.pageQuery(userQueryDTO.getPageNum(), userQueryDTO.getPageSize(), false, () -> {
            return this.userService.findUser(queryUserDO);
            });
        
        List<User> users = null;
        if (JudgeUtils.isNotNull(userDOs)){
            users = userDOs.stream().map(userDO -> {
                User user =  new UserQueryDTO.User();
                BeanUtils.copyProperties(user, userDO);
                return user;
            }).collect(Collectors.toCollection(ArrayList::new));
        }
        return GenericRspDTO.newSuccessInstance(users);
    }
    
    @ApiOperation(value="分页查找用户", notes="分页查找用户")
    @ApiResponse(code = 200, message = "用户信息")
    @GetMapping("/findUser3")
    public GenericRspDTO<List<User>> findUser3(@RequestParam String name, @RequestParam int pageNum, 
        @RequestParam int pageSize, @LemonBody GenericDTO<NoBody> genericDTO) {
        // UserQueryDTO userQueryDTO = queryUserDTO.getBody();
        //GenericDTO<NoBody> genericDTO2 = genericDTO;
        //查询对象
        UserDO queryUserDO = new UserDO();
        queryUserDO.setName(name);
        //以下是分页查询
        List<UserDO> userDOs = PageUtils.pageQuery(pageNum, pageSize, false, () -> {
            return this.userService.findUser(queryUserDO);
        });
        
        List<User> users = null;
        if (JudgeUtils.isNotNull(userDOs)){
            users = userDOs.stream().map(userDO -> {
                User user =  new UserQueryDTO.User();
                BeanUtils.copyProperties(user, userDO);
                return user;
            }).collect(Collectors.toCollection(ArrayList::new));
        }
        return GenericRspDTO.newSuccessInstance(users);
    }
    
    
    @ApiOperation(value="分页查找用户", notes="分页查找用户")
    @ApiResponse(code = 200, message = "用户信息")
    @GetMapping("/findUser4")
    public GenericRspDTO<List<User>> findUser4(@RequestParam String name, @RequestParam int pageSize, 
        @RequestParam int pageNum) {
        /*List<UserQueryDTO.User> userDOs = PageUtils.pageQuery(pageNum, pageSize, false, () -> {
            GenericRspDTO<List<UserQueryDTO.User>> grspDTO =  userClient.findUser3(name, pageSize, pageNum, GenericDTO.newInstance());
            return grspDTO.getBody();
        });*/
        UserQueryDTO userQueryDTO = GenericDTO.newInstance(UserQueryDTO.class);
        userQueryDTO.setName(name);
        userQueryDTO.setPageNum(pageNum);
        userQueryDTO.setPageSize(pageSize);
        GenericRspDTO<List<UserQueryDTO.User>> userDOs =  userClient.findUser2(userQueryDTO);
        return userDOs;
    }
    
    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));
        om.registerModule(javaTimeModule);
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
       // om.findAndRegisterModules();
        
        UserDO user = new UserDO();
        user.setAge(27);
        user.setBirthday(LocalDate.now());
        user.setName("yyyyy你好");
        user.setUserId("123456");
        String userStr = om.writeValueAsString(user);
        System.out.println(userStr);
        UserDO user2 = (UserDO) om.readValue(userStr.getBytes(), Object.class);
        System.out.println(user2);
    }
}
