package com.hisun.lemon.demo3.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.NoBody;

/**
 * User 传输对象
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午9:27:30
 *
 */
@ApiModel(value="UserRspDTO", description="响应用户传输对象")
public class UserRspDTO extends GenericRspDTO<NoBody>{
    
    @ApiModelProperty(value="姓名", required= true)
    @NotEmpty(message="DM310001")
    private String name;
    
    @ApiModelProperty(value="年龄", required= true)
    @Range(min=0,max=150,message="DM310002") 
    private Integer age;
    
    @ApiModelProperty(value="出生日期", required= true)
    @NotNull(message="DM310003")
    private LocalDate birthday;
    
    @ApiModelProperty(value="性别", required= true, allowableValues="男,女")
    @Pattern(regexp="男|女",message="DM310004")
    private String sex;
    
    @ApiModelProperty(value="分页查询-第几页", required= false)
    private Integer pageNum;
    
    @ApiModelProperty(value="分页查询-每页数据条数", required= false)
    private Integer pageSize;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
}
