package com.hisun.lemon.framework.dao;

import java.util.List;

import com.hisun.lemon.framework.data.BaseDO;

/**
 * dao 接口
 * 所有的DAO都必须继承或实现此接口
 * @author yuzhou
 * @date 2017年7月10日
 * @time 上午11:30:31
 *
 */
public interface BaseDao<T extends BaseDO> {
    /**
     * 根据ID查找
     * @param id
     * @return
     */
    public T get(String id);
    
    /**
     * 根据条件查找
     * @param entity
     * @return
     */
    public List<T> find(T entity);
    
    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(T entity);
    
    /**
     * 更新数据
     * @param entity
     * @return
     */
    public int update(T entity);
    
    /**
     * 删除数据
     * @param id
     * @return
     */
    public int delete(String id);
}
