package com.hisun.lemon.framework.schedule;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;

/**
 * 停止调度异常
 * @author yuzhou
 * @date 2017年9月30日
 * @time 下午3:16:11
 *
 */
public class StopSchedulingTaskException extends LemonException {
    private static final long serialVersionUID = -2031676730531856008L;

    public StopSchedulingTaskException() {
        super(ErrorMsgCode.SCHEDULE_TASK_EXCEPTION);
    }
}
