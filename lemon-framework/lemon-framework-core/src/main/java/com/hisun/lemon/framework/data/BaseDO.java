package com.hisun.lemon.framework.data;

import java.time.LocalDateTime;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.framework.annotation.PreInsert;
import com.hisun.lemon.framework.annotation.PreUpdate;

/**
 * DO 对象基础类，所有DO对象继承该类
 * Dao 方法insert* 自动执行preInsert
 * Dao 方法update* 自动执行preUpdate
 * @author yuzhou
 * @date 2017年6月16日
 * @time 下午4:26:20
 *
 */
public class BaseDO{
    //最后修改时间
    private LocalDateTime modifyTime;
    //创建时间
    private LocalDateTime createTime;
    
    @PreInsert
    public void preInsert() {
        if(null == this.getCreateTime()) {
            this.setCreateTime(DateTimeUtils.getCurrentLocalDateTime());
            this.setModifyTime(this.getCreateTime());
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        if(null == this.getModifyTime()) {
            this.setModifyTime(DateTimeUtils.getCurrentLocalDateTime());
        }
    }
    
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
}
