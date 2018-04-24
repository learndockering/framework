package com.hisun.lemon.framework.idgenerate.auto;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.utils.IdGenUtils;

/**
 * 默认生成策略
 * @author yuzhou
 * @date 2017年6月19日
 * @time 下午5:34:33
 *
 */
public class DefaultIdGenStrategy implements IdGenStrategy {
    
    @Override
    public String genId(String key, String prefix) {
        if(JudgeUtils.isBlank(prefix)){
            return IdGenUtils.generateCommonId(key);
        }
        return IdGenUtils.generateIdWithShortDate(key, prefix, IdGenUtils.getCommonIdSeqLength());
    }

}
