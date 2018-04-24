package com.hisun.lemon.framework.utils;

import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.idgenerate.IdGenerator;

/**
 * 
 * Id 生成工具类
 * 
 * @author yuzhou
 * @date 2017年6月15日
 * @time 下午4:41:16
 *
 */
public class IdGenUtils {
    private static final String IG_GENERATOR_BEAN_NAME  = "idGenerator";
    private static final String COMMON_ID_SEQUENCE_LENGTH = "lemon.idgen.common-id-sequence-len";
    private static final int DEFAULT_COMMON_ID_SEQ_LENGTH = 10;
    private static final char DEFAULT_SEQUENCE_PAD_CHAR = '0';
    private static final String MSG_ID_PREFIX_KEY = "MSGID_";
    private static final String MSG_ID_SEQUENCE_LENGTH = "lemon.idgen.msg-id-sequence-len";
    private static final int DEFAULT_MSG_ID_LENGTH_SEQ = 10;
    private static final String REQUEST_ID_PREFIX_KEY = "REQUESTID_";
    private static final String REQUEST_ID_SEQUENCE_LENGTH = "lemon.idgen.request-id-sequence-len";
    private static final int DEFAULT_REQUEST_ID_LENGTH_SEQ = 10;
    
    private static IdGenerator idGenerator;
    private static Integer commonIdSeqLength;
    private static Integer msgIdSeqLength;
    private static Integer requestIdSeqLength;
    
    
    /**
     * 生成Id
     * @param key
     * @return
     */
    public static Long generateId(String key) {
        return getGenerator().generateId(key);
    }
    
    /**
     * Id = prefix + id
     * @param key
     * @param prefix
     * @return
     */
    public static String generateId(String key, String prefix) {
        return prefix + generateId(key);
    }
    
    /**
     * @param key
     * @param prefix ID前缀
     * @param length 序列号的长度
     * @return
     */
    public static String generateId(String key, String prefix, int length) {
        return prefix + StringUtils.leftPad(generateId(key).toString(), length, getPaddingChar());
    }
    
    /**
     * @param key
     * @param length 序列号的长度
     * @return
     */
    public static String generateId(String key, int length) {
        return StringUtils.leftPad(generateId(key).toString(), length, getPaddingChar());
    }
    
    /**
     * @param key
     * @param length 序列号的长度
     * @return
     */
    public static String generateReversedId(String key, int length) {
        return StringUtils.leftPad(StringUtils.reverse(generateId(key).toString()), length, getPaddingChar());
    }
    
    /**
     * prefix + yyyymmdd + id
     * 
     * @param key
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithDate(String key, String prefix, int length) {
        return prefix + DateTimeUtils.getCurrentDateStr() + generateId(key, length);
    }
    
    /**
     * prefix + yymmdd + id
     * 
     * @param key
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithShortDate(String key, String prefix, int length) {
        return prefix + DateTimeUtils.getCurrentShortDateStr() + generateId(key, length);
    }
    
    /**
     * yyyymmdd + id
     * 
     * @param key
     * @param length
     * @return
     */
    public static String generateIdWithDate(String key, int length) {
        return DateTimeUtils.getCurrentDateStr() + generateId(key, length);
    }
    
    /**
     * yymmdd + id
     * 
     * @param key
     * @param length
     * @return
     */
    public static String generateIdWithShortDate(String key, int length) {
        return DateTimeUtils.getCurrentShortDateTimeStr() + generateId(key, length);
    }
    
    /**
     * prefix + yyyymmddHHmmss + id
     * 
     * @param key
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithDateTime(String key, String prefix, int length) {
        return prefix + DateTimeUtils.getCurrentDateTimeStr() + generateId(key, length);
    }
    
    /**
     * prefix + yymmddHHmmss + id
     * 
     * @param key
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithShortDateTime(String key, String prefix, int length) {
        return prefix + DateTimeUtils.getCurrentShortDateTimeStr() + generateId(key, length);
    }
    
    /**
     * yyyymmddHHmmss + id
     * 
     * @param key
     * @param length
     * @return
     */
    public static String generateIdWithDateTime(String key, int length) {
        return DateTimeUtils.getCurrentDateTimeStr() + generateId(key, length);
    }
    
    /**
     * yymmddHHmmss + id
     * 
     * @param key
     * @param length
     * @return
     */
    public static String generateIdWithShortDateTime(String key, int length) {
        return DateTimeUtils.getCurrentShortDateTimeStr() + generateId(key, length);
    }
    
    /**
     * 通用的Id生成方法
     * key 在同一应用内不能重复
     * @param key
     * @return
     */
    public static String generateCommonId(String key) {
        return generateIdWithShortDate(key, getCommonIdSeqLength());
    }
    
    /**
     * 生成交易流水号
     * @return
     */
    public static String generateMsgId() {
        return LemonUtils.getApplicationName() + generateIdWithShortDateTime(MSG_ID_PREFIX_KEY, getMsgIdSeqLength());
    }
    
    /**
     * 生成请求流水号
     * @return
     */
    public static String generateRequestId() {
        return LemonUtils.getApplicationName() + generateIdWithShortDateTime(REQUEST_ID_PREFIX_KEY, getRequestIdSeqLength());
    }
    
    /**
     * 对所有应用唯一
     * @param key
     * @return
     */
    public static Long generatorGlobalId(String key) {
        return getGenerator().generateGlobalId(key);
    }
    
    private static IdGenerator getGenerator() {
        if(null == idGenerator) {
            idGenerator = ExtensionLoader.getSpringBean(IG_GENERATOR_BEAN_NAME, IdGenerator.class);
        }
        return idGenerator;
    }
    
    public static int getCommonIdSeqLength() {
        if(null == commonIdSeqLength) {
            commonIdSeqLength = LemonUtils.getProperty(COMMON_ID_SEQUENCE_LENGTH, Integer.class);
            if(null  ==  commonIdSeqLength) {
                commonIdSeqLength = DEFAULT_COMMON_ID_SEQ_LENGTH;
            }
        }
        return commonIdSeqLength;
    }
    
    public static int getMsgIdSeqLength() {
        if(null == msgIdSeqLength) {
            msgIdSeqLength = LemonUtils.getProperty(MSG_ID_SEQUENCE_LENGTH, Integer.class);
            if(null  ==  msgIdSeqLength) {
                msgIdSeqLength = DEFAULT_MSG_ID_LENGTH_SEQ;
            }
        }
        return msgIdSeqLength;
    }
    
    public static int getRequestIdSeqLength() {
        if(null == requestIdSeqLength) {
            requestIdSeqLength = LemonUtils.getProperty(REQUEST_ID_SEQUENCE_LENGTH, Integer.class);
            if(null  ==  requestIdSeqLength) {
                requestIdSeqLength = DEFAULT_REQUEST_ID_LENGTH_SEQ;
            }
        }
        return requestIdSeqLength;
    }
    
    public static char getPaddingChar() {
        return DEFAULT_SEQUENCE_PAD_CHAR;
    }
    
}
