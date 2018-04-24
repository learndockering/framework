package com.hisun.lemon.demo3.controller;

import io.swagger.annotations.Api;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hisun.lemon.framework.i18n.LocaleMessageSource;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 模板测试
 * i18n 测试
 * @author yuzhou
 * @date 2017年6月23日
 * @time 下午5:48:04
 *
 */
@Controller
@RequestMapping("/template")
@Api(tags="模版交易")
public class TemplateController {
    
    @Resource
    private LocaleMessageSource localeMessageSource;
    /**
     * 返回html模板.
     */
    @GetMapping("/helloHtml")
    public String helloHtml(Map<String,Object> map){
      // map.put("hello","from TemplateController.helloHtml");
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println("Locale====="+locale);
        System.out.println("Locale~~~~~~"+LemonUtils.getLocale());
        System.out.println("welcome===="+localeMessageSource.getMessage("welcome"));
       return"/helloHtml";
    }
    
}
