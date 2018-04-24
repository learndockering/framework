package com.hisun.lemon.common;

import java.util.Properties;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ResourceUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * lemon sytem
 * @author yuzhou
 * @date 2017年7月21日
 * @time 下午2:57:20
 *
 */
public class LemonFramework {
    private static final Logger logger = LoggerFactory.getLogger(LemonFramework.class);
    
    public static final String BATCH_ENV = "lemon.batch.enabled";
    public static final String REGISTER_WITH_EUREKA = "registerWithEureka";
    //public static final String REGISTER_WITH_EUREKA = "eureka.client.registerWithEureka";

    /**
     * 
     * spring boot application 需要调用此方法
     * 
     * @param clazz
     * @param args
     */
    public static void processApplicationArgs(Class<?> clazz, String[] args) {
        processApplicationArgs(args);
        SpringApplication.run(clazz, args);
    }
    
    /**
     * 处理lemon 平台参数
     * @param args
     */
    public static void processApplicationArgs(String[] args) {
        if(JudgeUtils.isBlank(System.getProperty("lemon.home"))) {
            if(logger.isWarnEnabled()) {
                logger.warn("Env variable \"lemon.home\" is not defined.");
                System.out.println("Env variable \"lemon.home\" is not defined.");
            }
        }
        
        if (JudgeUtils.equalsIgnoreCase(System.getProperty(BATCH_ENV), "true")) {
            System.setProperty(REGISTER_WITH_EUREKA, "false");
        } else {
            System.setProperty(REGISTER_WITH_EUREKA, "true");
        }
        
        String env = Env.getProfile(Env.getDefaultEnv());
        boolean hasProfilesActiveArg = false;
        if(args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++ ) {
                String arg = args[i];
                if(StringUtils.startsWith(arg, "--spring.profiles.active=")) {
                    env = arg.split("=")[1];
                    if(JudgeUtils.isNotBlank(env)) {
                        hasProfilesActiveArg = true;
                    }
                    break;
                }
            }
        }
        if(! hasProfilesActiveArg) {
            if(JudgeUtils.isBlank(System.getProperty("spring.profiles.active"))) {
                System.setProperty("spring.profiles.active", StringUtils.getDefaultIfEmpty(env, Env.getProfile(Env.getDefaultEnv())));
                if(logger.isWarnEnabled()) {
                    logger.warn("Param \"spring.profiles.active\" does not exists. set default evn \"{}\"", Env.getProfile(Env.getDefaultEnv()));
                }
            }
        }
        if(JudgeUtils.isBlank(System.getProperty("lemon.log.path"))) {
            System.setProperty("lemon.log.path", "../logs");
        }
        
        Properties properties = System.getProperties();
        if(logger.isInfoEnabled()) {
            StringBuilder propertiesSb = new StringBuilder();
            properties.stringPropertyNames().forEach(key -> propertiesSb.append(key).append(" : ")
                .append(System.getProperty(key)).append(ResourceUtils.ENTER_NEW_LINE));
            logger.info("Show system properties: {} {}", ResourceUtils.ENTER_NEW_LINE, propertiesSb.toString());
            propertiesSb.delete(0, propertiesSb.length());
            if(args != null) {
                Stream.of(args).parallel().forEach(arg -> propertiesSb.append(arg).append(ResourceUtils.ENTER_NEW_LINE));
                logger.info("Show start server args: {} {}",ResourceUtils.ENTER_NEW_LINE, propertiesSb.toString());
            }
            propertiesSb.delete(0, propertiesSb.length());
        }
    }
}
