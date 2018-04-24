package com.hisun.lemon.framework.web.converter;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * localDate to String 
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:12:56
 *
 */
public class LocalDateToStringConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate source) {
        if (JudgeUtils.isNull(source)) {
            return null;
        }
        return DateTimeUtils.formatLocalDate(source);
    }

}
