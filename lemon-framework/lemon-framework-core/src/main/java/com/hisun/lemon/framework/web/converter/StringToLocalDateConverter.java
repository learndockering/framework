package com.hisun.lemon.framework.web.converter;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * String 转 localDate
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private static final int DATE_LENGTH = 8;
    @Override
    public LocalDate convert(String source) {
        if (JudgeUtils.isBlank(source)) {
            return null;
        }
        if(StringUtils.length(source) != DATE_LENGTH) {
            throw new LemonException(LemonException.SYS_ERROR_MSGCD, "Date string length must equals 8.");
        }
        source = StringUtils.trim(source);
        return DateTimeUtils.parseLocalDate(source);
    }

}
