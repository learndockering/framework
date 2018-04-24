package com.hisun.lemon.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.hisun.lemon.framework.entity.MsgInfoDO;

/**
 * 消息码信息DAO
 * @author yuzhou
 * @date 2017年6月27日
 * @time 下午1:31:28
 *
 */
@Mapper
public interface IMsgInfoDao {
    
    /**
     * 根据msgCd 、language 查找消息码信息
     * @param msgCd
     * @param language
     * @return
     */
    @Select("SELECT MSG_CD, LANGUAGE, SCENARIO, MSG_INFO FROM LEMON_MSG_INFO WHERE MSG_CD = #{msgCd} AND LANGUAGE = #{language}")
    @Results({
        @Result(column="msg_cd", property="msgCd", id=true),  
        @Result(column="language", property="language"), 
        @Result(column="scenario", property="scenario"), 
        @Result(column="msg_info", property="msgInfo")
    }) 
    public List<MsgInfoDO> findMsgInfos(@Param("msgCd") String msgCd, @Param("language") String language);
}
