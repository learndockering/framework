package com.hisun.lemon.framework.i18n;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 消息资源
 * 
 * @see org.springframework.context.MessageSource
 * @author yuzhou
 * @date 2017年6月26日
 * @time 上午11:31:00
 *
 */
@Component
public class LocaleMessageSource{
    
    @Resource
    private MessageSource messageSource;

    public String getMessage(String code, Object[] args, String defaultMessage) {
        return this.messageSource.getMessage(code, args, defaultMessage, LemonUtils.getLocale());
    }
    
    public String getMessage(String code, String defaultMessage) {
        return this.messageSource.getMessage(code, null, defaultMessage, LemonUtils.getLocale());
    }

    public String getMessage(String code, Object[] args)
            throws NoSuchMessageException {
        return this.messageSource.getMessage(code, args, LemonUtils.getLocale());
    }
    
    public String getMessage(String code)
            throws NoSuchMessageException {
        return this.messageSource.getMessage(code, null, LemonUtils.getLocale());
    }

    public String getMessage(MessageSourceResolvable resolvable)
            throws NoSuchMessageException {
        return this.messageSource.getMessage(resolvable, LemonUtils.getLocale());
    }

}
