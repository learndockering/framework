package com.hisun.lemon.demo3.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.hisun.lemon.demo3.entity.UserDO;
import com.hisun.lemon.framework.dao.BaseDao;

/**
 * @author yuzhou
 * @date 2017年6月7日
 * @time 下午1:54:26
 *
 */
@Mapper
public interface IUserDao extends BaseDao<UserDO>{
    
/*    @Insert("INSERT INTO USER(USER_ID, NAME, SEX, BIRTHDAY, AGE, CREATE_TIME, MODIFY_TIME) VALUES(#{userId}, #{name}, #{sex}, #{birthday, javaType=java.time.LocalDate,jdbcType=DATE}, #{age}, #{createTime, javaType=java.time.LocalDateTime,jdbcType=TIMESTAMP}, #{modifyTime, javaType=java.time.LocalDateTime,jdbcType=TIMESTAMP})")
    int insert(UserDO userPO);
    */
    @Select("SELECT USER_ID, NAME, SEX, BIRTHDAY, AGE, CREATE_TIME, MODIFY_TIME FROM USER WHERE NAME = #{name}")
    UserDO findByName(@Param("name") String name);
    
    @Select("SELECT USER_ID, NAME, SEX, BIRTHDAY, AGE, CREATE_TIME, MODIFY_TIME FROM USER WHERE USER_ID = #{userId}")
    UserDO findByUserId(@Param("userId") String userId);
    
}
