package com.hisun.lemon.framework.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DefaultInput {
    String DEFAULT_INPUT = "input";
    
    @Input(DefaultInput.DEFAULT_INPUT)
    SubscribableChannel defaultInput();
}
