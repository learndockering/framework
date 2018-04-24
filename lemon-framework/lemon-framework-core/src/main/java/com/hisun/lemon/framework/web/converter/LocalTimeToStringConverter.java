package com.hisun.lemon.framework.web.converter;

import java.time.LocalTime;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * LocalTime to String
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class LocalTimeToStringConverter implements Converter<LocalTime, String> {
    
    @Override
    public String convert(LocalTime source) {
        if (JudgeUtils.isNull(source)) {
            return null;
        }
        return DateTimeUtils.formatLocalTime(source);
    }

}
