package com.hisun.lemon.swagger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.hisun.lemon.common.Env;
import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * swagger enable condition
 * @author yuzhou
 * @date 2017年9月19日
 * @time 下午3:34:35
 *
 */
public class SwaggerEnableCondition extends SpringBootCondition {
    public static final String SWAGGER_PROFILES_ACTIVE = "swagger.profiles";
    
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
            AnnotatedTypeMetadata metadata) {
        boolean enabled = isEnabledSwagger(context);
        ConditionMessage.Builder message = ConditionMessage
                .forCondition("SwaggerEnabled");
        if (enabled) {
            return ConditionOutcome.match(message.because("swagger enabled"));
        }
        return ConditionOutcome.noMatch(message.because("swagger disabled"));
    }

    public boolean isEnabledSwagger(ConditionContext context) {
        String profiles = context.getEnvironment().getProperty(SWAGGER_PROFILES_ACTIVE);
        String currentEnv = context.getEnvironment().getProperty(LemonConstants.CURRENT_ENV);
        boolean enabled = false;
        if(JudgeUtils.isNotBlankAll(profiles, currentEnv)) {
            List<?> list = Stream.of(profiles.split(",")).filter(env -> StringUtils.equals(env, currentEnv)).collect(Collectors.toList());
            if(JudgeUtils.isNotEmpty(list)) {
                enabled = true;
            }
        }
        if(JudgeUtils.isBlank(profiles) && JudgeUtils.isNotBlank(currentEnv)) {
            if(! Env.isPrd(Env.valueOf(currentEnv.toUpperCase()))) {
                enabled = true;
            }
        }
        return enabled;
    }
}
