package com.hisun.lemon.framework.web.converter;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * String 转 LocalDateTime
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private static final int DATE_TIME_LENGTH = 14;
    
    @Override
    public LocalDateTime convert(String source) {
        if (JudgeUtils.isBlank(source)) {
            return null;
        }
        source = StringUtils.trim(source);
        if(StringUtils.length(source) != DATE_TIME_LENGTH) {
            throw new LemonException(LemonException.SYS_ERROR_MSGCD, "Date time string length must equals 14.");
        }
        return DateTimeUtils.parseLocalDateTime(source);
    }

}
