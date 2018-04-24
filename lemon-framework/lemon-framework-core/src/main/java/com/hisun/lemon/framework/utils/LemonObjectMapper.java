package com.hisun.lemon.framework.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

/**
 * ObjectMapper封装
 * @author yuzhou
 * @date 2017年8月4日
 * @time 下午3:27:32
 *
 */
public class LemonObjectMapper extends ObjectMapper{
    private static final long serialVersionUID = 225099494082063482L;

    public LemonObjectMapper(ObjectMapper objectMapper) {
        super(objectMapper);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_TIME));
        registerModule(javaTimeModule);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
}
