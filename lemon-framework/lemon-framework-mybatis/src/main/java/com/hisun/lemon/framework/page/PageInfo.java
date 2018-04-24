package com.hisun.lemon.framework.page;

import java.util.List;


/**
 * @author yuzhou
 * @date 2017年10月18日
 * @time 下午1:05:57
 *
 * @param <T>
 */
public class PageInfo<T> extends com.github.pagehelper.PageInfo<T> {
    private static final long serialVersionUID = -7715300061217223096L;

    public PageInfo(List<T> list) {
        super(list);
    }
}
