package com.hisun.lemon.framework.idgenerate.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuzhou
 * @date 2017年9月1日
 * @time 上午11:52:05
 *
 */
@ConfigurationProperties(prefix = "lemon.idgen")
public class IdGenProperties {
    public final static String DEFAULT_DELTA_KEY = "default";
    
    private Map<String, Integer> delta = new LinkedHashMap<>();
    private String prefix;
    private AutoGen autogen;
    
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public AutoGen getAutogen() {
        return autogen;
    }
    public void setAutogen(AutoGen autogen) {
        this.autogen = autogen;
    }

    public Map<String, Integer> getDelta() {
        return delta;
    }
    public void setDelta(Map<String, Integer> delta) {
        this.delta = delta;
    }

    public static class AutoGen {
        private String DOPackage;

        public String getDOPackage() {
            return DOPackage;
        }

        public void setDOPackage(String dOPackage) {
            DOPackage = dOPackage;
        }
        
    }
    
}
