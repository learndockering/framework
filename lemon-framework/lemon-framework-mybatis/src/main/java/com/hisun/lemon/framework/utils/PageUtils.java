package com.hisun.lemon.framework.utils;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hisun.lemon.common.Callback;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.page.PageInfo;

/**
 * 分页辅助类
 * @author yuzhou
 * @date 2017年7月11日
 * @time 下午5:43:30
 *
 */
public class PageUtils {
    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 8;
    private static final String CONFIG_PAGE_NUM = LemonUtils.getProperty("pagehelper.defaultPageNum");
    private static final String CONFIG_PAGE_SIZE = LemonUtils.getProperty("pagehelper.defaultPageSize");
    /**
     * 分页查询
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @param count     是否进行count操作，原则上都不允许count操作，请设置为false
     * @param callback  回调查询
     * 
     * @return   分页查询结果
     */
    public static <T> List<T> pageQuery(int pageNum, int pageSize, boolean count, Callback<List<T>> callback) {
        PageHelper.startPage(pageNum, pageSize, count);
        try {
            return callback.callback();
        } finally {
            PageHelper.clearPage();
        }
    }
    
    /**
     * 分页查询
     * 不进行count计算
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @param callback  回调查询
     * 
     * @return   分页查询结果
     */
    public static <T> List<T> pageQuery(int pageNum, int pageSize, Callback<List<T>> callback) {
        return pageQuery(pageNum, pageSize, false, callback);
    }
    
    /**
     * 分页查询
     * 进行count计算
     * @param pageNum
     * @param pageSize
     * @param callback
     * @return
     */
    public static <T> PageInfo<T> pageQueryWithCount(int pageNum, int pageSize, Callback<List<T>> callback) {
        Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
        try {
            callback.callback();
            return new PageInfo<T>(page);
        } finally {
            PageHelper.clearPage();
        }
    }
    
    /**
     * 默认每页数量
     * @return
     */
    public static int getDefaultPageSize() {
        if(JudgeUtils.isNotBlank(CONFIG_PAGE_SIZE)) {
            return Integer.valueOf(CONFIG_PAGE_SIZE);
        }
        return DEFAULT_PAGE_SIZE;
    }
    
    /**
     * 默认页数
     * @return
     */
    public static int getDefaultPageNum() {
        if(JudgeUtils.isNotBlank(CONFIG_PAGE_NUM)) {
            return Integer.valueOf(CONFIG_PAGE_NUM);
        }
        return DEFAULT_PAGE_NUM;
    }
}
