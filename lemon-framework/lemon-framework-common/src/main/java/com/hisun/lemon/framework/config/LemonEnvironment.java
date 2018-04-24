package com.hisun.lemon.framework.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * 当前系统环境变量及配置文件
 * 
 * @author yuzhou
 * @date 2017年7月6日
 * @time 上午10:04:25
 *
 */
@Configuration("lemonEnvironment")
@EnableConfigurationProperties(LemonEnvironment.LemonConfig.class)
public class LemonEnvironment implements EnvironmentAware{
    private static final Logger logger = LoggerFactory.getLogger(LemonEnvironment.class);
    
    private Environment environment;
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    private Locale defaultLocale;
    
    @Autowired
    private LemonConfig lemonConfig;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    
    public String getProperty(String key) {
        return this.environment.getProperty(key);
    }
    
    public <T> T getProperty(String key, Class<T> targetType) {
        return this.environment.getProperty(key, targetType);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    /**
     * 系统默认的Locale
     * @return
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }
    
    public List<Locale> getSupportLocales() {
        return this.lemonConfig.getLocale().getSupportLocales();
    }

    public LemonConfig getLemonConfig() {
        return lemonConfig;
    }

    public void setLemonConfig(LemonConfig lemonConfig) {
        this.lemonConfig = lemonConfig;
    }

    @PostConstruct
    public void postConstruct() {
       /* if(JudgeUtils.isNotBlank(this.defaultLocaleString)) {
            String[] languageAndCountry = this.defaultLocaleString.split("_");
            this.defaultLocale = new Locale(languageAndCountry[0], (languageAndCountry.length == 2) ? languageAndCountry[1] : "");
        }*/
        this.defaultLocale = this.lemonConfig.getLocale().getDefaultLocale();
        if(JudgeUtils.isEmpty(this.getLemonConfig().getGateways())) {
            this.getLemonConfig().setGateways(new String[]{"AGW","IGW"});
        }
        if(logger.isInfoEnabled()) {
            logger.info("default locale is {}.", this.defaultLocale);
            logger.info("support locales are {}", this.lemonConfig.getLocale().getSupportLocales());
            logger.info("support gateways are {}", Arrays.toString(this.getLemonConfig().getGateways()));
        }
    }
    
    @ConfigurationProperties(prefix = "lemon")
    public class LemonConfig {
        private LocaleConfig locale;
        private String[] gateways;

        public LocaleConfig getLocale() {
            return locale;
        }

        public void setLocale(LocaleConfig locale) {
            this.locale = locale;
        }

        public String[] getGateways() {
            return gateways;
        }

        public void setGateways(String[] gateways) {
            this.gateways = gateways;
        }
        
    }
    
    public static class LocaleConfig {
        private Locale defaultLocale;
        private List<Locale> supportLocales;
        public Locale getDefaultLocale() {
            return defaultLocale;
        }
        public void setDefaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
        }
        public List<Locale> getSupportLocales() {
            return supportLocales;
        }
        public void setSupportLocales(List<Locale> supportLocales) {
            this.supportLocales = supportLocales;
        }
    }
    
}
