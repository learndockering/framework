package com.hisun.lemon.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * 
 * 字符串处理
 * 所有关于字符串的处理必须调用此类
 * 
 * @author yuzhou
 * @date 2017年6月13日
 * @time 下午5:23:36
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 如果字符串str 为空，则取默认值defaultStr
     * @param str
     * @param defaultStr
     * @return
     */
    public static String getDefaultIfEmpty(String str, String defaultValueStr){
        if(null == str || "".equals(str)){
            return defaultValueStr;
        }
        return str;
    }
    
    /**
     * 对象转字符串
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        if(null == obj){
            return null; //之前返回null
        }
        return String.valueOf(obj);
    }
    
    public static String leftPadIfNecessary(String value, int length, char padChar){
        if(-1 == length){
            return value;
        }
        return leftPad(value, length, padChar);
    }
    
    /**
     * 右补充达到指定长度
     * 
     * @param value
     * @param length
     * @param padChar
     * @return
     */
    public static String rightPadIfNecessary(String value, int length, char padChar){
        if(-1 == length){
            return value;
        }
        return rightPad(value, length, padChar);
    }
    
    /**
     * 删除左补充字符
     * 
     * @param value
     * @param padChar
     * @return
     */
    public static String removeLeftPad(String value, char padChar){
        if(StringUtils.isEmpty(value)){
            return value;
        }
        int index = 0;
        char[] valueChars = value.toCharArray();
        for(int i = 0; i < valueChars.length; i++) {
            if(valueChars[i] != padChar) {
                index = i;
                break;
            }
        }
        return value.substring(index);
    }
    
    /**
     * 删除右补充字符
     * @param value
     * @param padChar
     * @return
     */
    public static String removeRightPad(String value, char padChar) {
        if(StringUtils.isEmpty(value)){
            return value;
        }
        int index = value.length() -1;
        char[] valueChars = value.toCharArray();
        for(int i = value.length() - 1; i >= 0; i--){
            if(valueChars[i] != padChar) {
                index = i+1;
                break;
            }
        }
        return value.substring(0, index);
    }
    
    /**
     * 异常对象转化为字符串
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName() + ": ");
        if (e.getMessage() != null) {
            p.print(e.getMessage() + "\n");
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }
    
    /**
     * 解决占位符
     * @param originalStr
     * @param environment
     * @return
     */
    public static String parsePlaceHolder(String originalStr, Environment environment) {
        if(JudgeUtils.isBlank(originalStr)) {
            return originalStr;
        }
        StringBuilder sb = new StringBuilder();
        String str = originalStr;
        while ( true) {
            int start = str.indexOf(PropertySourcesPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX);
            if ( -1 == start) {
                break;
            }
            sb.append(str.substring(0, start));
            int end = str.indexOf("}");
            sb.append(environment.getProperty(str.substring(start+2,end)));
            sb.append(str.substring(end+1));
            str = sb.toString();
            sb.delete(0, sb.length());
        }
        return str;
    }
}
