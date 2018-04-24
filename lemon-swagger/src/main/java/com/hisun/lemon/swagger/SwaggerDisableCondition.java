package com.hisun.lemon.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * swagger disable condition
 * @author yuzhou
 * @date 2017年9月19日
 * @time 下午3:34:35
 *
 */
public class SwaggerDisableCondition extends SwaggerEnableCondition {
    public static final String SWAGGER_PROFILES_ACTIVE = "swagger.profiles";
    
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
            AnnotatedTypeMetadata metadata) {
        boolean enabled = isEnabledSwagger(context);
        ConditionMessage.Builder message = ConditionMessage
                .forCondition("SwaggerDisabled");
        if (enabled) {
            return ConditionOutcome.noMatch(message.because("swagger disabled"));
        }
        return ConditionOutcome.match(message.because("swagger enabled"));
    }
}
