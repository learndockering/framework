package com.hisun.lemon.framework.cumulative;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;


/**
 * 累计
 * <ul>
 * <li>日累计
 * <li>月累计
 * </ul>
 * @author yuzhou
 * @date 2017年7月20日
 * @time 下午3:37:05
 *
 */
public interface Cumulative {
    
    /**
     * 日累计
     * @param key
     * @param dimensions
     * @return
     */
    void countByDay(String key, Dimension... dimensions);
    
    /**
     * 月累计
     * @param key
     * @param dimensions
     * @return
     */
    void countByMonth(String key, Dimension... dimensions);
    
    /**
     * 日月累计
     * @param key
     * @param dimensions
     * @return
     */
    void countByDayAndMonth(String key, Dimension... dimensions);
    
    /**
     * 查询日累计
     * @param key
     * @param dimensionKey 维度key
     * @return
     */
    String queryByDay(String key, String dimensionKey);
    
    /**
     * 查询月累计
     * @param key
     * @param dimensionKey 维度key
     * @return
     */
    String queryByMonth(String key, String dimensionKey);
    
    default String getKey(String prefix, String key, CumulativeMode mode) {
        StringBuilder sb = new StringBuilder(prefix);
        switch (mode) {
        case DAY:
            sb.append(DateTimeUtils.getCurrentDateStr());
            break;
        case MONTH:
            sb.append(DateTimeUtils.getCurrentMonthStr());
            break;
        default:
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(),"can not get cumulative key with mode "+mode);
            break;
        }
        sb.append(".").append(key);
        return sb.toString();
    }
    
    /**
     * 累计模式
     * @author yuzhou
     * @date 2017年7月20日
     * @time 下午6:59:12
     *
     */
    enum CumulativeMode {
        DAY("0"), MONTH("1"), DAY_AND_MONTH("2");
        String mode;
        private CumulativeMode(String mode) {
            this.mode = mode;
        }
        public String getMode() {
            return this.mode;
        }
    }
    
    /**
     * 维度
     * @author yuzhou
     * @date 2017年7月20日
     * @time 下午3:46:49
     *
     */
    public static class Dimension {
        private String key;
        private String value;
        /**
         * @param key    累计维度
         * @param value  累计值
         */
        public Dimension(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}


