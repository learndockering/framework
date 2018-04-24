package com.hisun.lemon.framework.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 上午11:13:20
 *
 */
public class InitLemonDataConfigurationSelector extends AdviceModeImportSelector<EnableInitLemonData> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
                return getAspectJImports();
            default:
                return null;
        }
    }

    private String[] getProxyImports() {
        List<String> result = new ArrayList<String>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(ProxyInitLemonDataConfiguration.class.getName());
        return result.toArray(new String[result.size()]);
    }
    
    private String[] getAspectJImports() {
        throw LemonException.create(ErrorMsgCode.SYS_ERROR.getMsgCd(), "@InitLemonData not support aspectJ mode.");
    }
}
