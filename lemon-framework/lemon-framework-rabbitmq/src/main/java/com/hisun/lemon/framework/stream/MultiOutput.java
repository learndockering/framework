package com.hisun.lemon.framework.stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

/**
 * 多通道
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午6:53:19
 *
 */
@Configuration
public class MultiOutput {
    public static final String OUTPUT_DEFAULT = "output";
    public static final String OUTPUT_ONE = "output1";
    public static final String OUTPUT_TWO = "output2";
    public static final String OUTPUT_THREE = "output3";
    public static final String OUTPUT_FOUR = "output4";
    public static final String OUTPUT_FIVE = "output5";
    public static final String OUTPUT_SIX = "output6";
    public static final String OUTPUT_SEVEN = "output7";
    
    @ConditionalOnProperty(value=DefaultSender.ENABLE, matchIfMissing= false)
    @EnableBinding(DefaultSender.class)
    public static interface DefaultSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_DEFAULT+".enable";
        
        @Output(MultiOutput.OUTPUT_DEFAULT)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=OneSender.ENABLE, matchIfMissing= false)
    @EnableBinding(OneSender.class)
    public static interface OneSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_ONE+".enable";
        
        @Output(MultiOutput.OUTPUT_ONE)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=TwoSender.ENABLE, matchIfMissing= false)
    @EnableBinding(TwoSender.class)
    public static interface TwoSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_TWO+".enable";
        
        @Output(MultiOutput.OUTPUT_TWO)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=ThreeSender.ENABLE, matchIfMissing= false)
    @EnableBinding(ThreeSender.class)
    public static interface ThreeSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_THREE+".enable";
        
        @Output(MultiOutput.OUTPUT_THREE)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=FourSender.ENABLE, matchIfMissing= false)
    @EnableBinding(FourSender.class)
    public static interface FourSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_FOUR+".enable";
        
        @Output(MultiOutput.OUTPUT_FOUR)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=FiveSender.ENABLE, matchIfMissing= false)
    @EnableBinding(FiveSender.class)
    public static interface FiveSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_FIVE+".enable";
        
        @Output(MultiOutput.OUTPUT_FIVE)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=SixSender.ENABLE, matchIfMissing= false)
    @EnableBinding(SixSender.class)
    public static interface SixSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_SIX+".enable";
        
        @Output(MultiOutput.OUTPUT_SIX)
        MessageChannel output();
    }
    
    @ConditionalOnProperty(value=SevenSender.ENABLE, matchIfMissing= false)
    @EnableBinding(SevenSender.class)
    public static interface SevenSender {
        public static final String ENABLE = "spring.cloud.stream.bindings."+MultiOutput.OUTPUT_SEVEN+".enable";
        
        @Output(MultiOutput.OUTPUT_SEVEN)
        MessageChannel output();
    }
}
