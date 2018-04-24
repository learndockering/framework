package com.hisun.lemon.framework.web.converter;

import java.time.LocalTime;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * String 转 LocalTime
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    private static final int TIME_LENGTH = 6;
    
    @Override
    public LocalTime convert(String source) {
        if (JudgeUtils.isBlank(source)) {
            return null;
        }
        source = StringUtils.trim(source);
        if(StringUtils.length(source) != TIME_LENGTH) {
            throw new LemonException(LemonException.SYS_ERROR_MSGCD, "time string length must equals 6.");
        }
        return DateTimeUtils.parseLocalTime(source);
    }

}
