package com.hisun.lemon.framework.web.converter;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * LocalDateTime to String
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime source) {
        if (JudgeUtils.isNull(source)) {
            return null;
        }
        return DateTimeUtils.formatLocalDateTime(source);
    }

}
