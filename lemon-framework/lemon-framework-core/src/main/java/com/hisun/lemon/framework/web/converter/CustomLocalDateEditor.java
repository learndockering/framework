package com.hisun.lemon.framework.web.converter;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.hisun.lemon.common.utils.DateTimeUtils;

/**
 * LocalDate
 * @author yuzhou
 * @date 2017年6月27日
 * @time 上午10:10:36
 *
 */
public class CustomLocalDateEditor extends PropertyEditorSupport {
    private final boolean allowEmpty;
    private final int exactDateLength;
    
    public CustomLocalDateEditor(boolean allowEmpty, int exactDateLength) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        } else {
            setValue(DateTimeUtils.parseLocalDate(text));
        }
        
    }
    
    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null ? DateTimeUtils.formatLocalDate(value) : "");
    }
}
