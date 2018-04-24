package com.hisun.lemon.framework.controller;

import javax.annotation.Resource;

import com.hisun.lemon.framework.i18n.LocaleMessageSource;

/**
 * controller 层基类
 * 
 * @author yuzhou
 * @date 2017年7月10日
 * @time 上午11:47:28
 *
 */
public class BaseController {
    
    /**
     * 国际化资源
     */
    @Resource
    private LocaleMessageSource localeMessageSource;
    
    
}
