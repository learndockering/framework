package com.hisun.lemon.common.condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.hisun.lemon.common.LemonFramework;

/**
 * @author yuzhou
 * @date 2017年10月25日
 * @time 上午10:13:55
 *
 */
public class DisabledBatchConditional extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Boolean batchEnabled = context.getEnvironment().getProperty(LemonFramework.BATCH_ENV, Boolean.class, false);
        ConditionMessage.Builder message = ConditionMessage.forCondition("BatchDisabled");
        if (batchEnabled) {
            return ConditionOutcome.noMatch(message.because("batch enabled"));
        }
        return ConditionOutcome.match(message.because("batch disabled"));
    }

}
