package com.hisun.lemon.demo3.entity;

import java.time.LocalDate;

import com.hisun.lemon.framework.data.BaseDO;
import com.hisun.lemon.framework.idgenerate.auto.AutoIdGen;

/**
 * UserDO
 * @author yuzhou
 * @date 2017年6月7日
 * @time 下午2:08:21
 *
 */
public class UserDO extends BaseDO{
    @AutoIdGen(key="USER_ID", prefix="US")
    private String userId;
    private String name;
    private String sex;
    private LocalDate birthday;
    private Integer age;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
