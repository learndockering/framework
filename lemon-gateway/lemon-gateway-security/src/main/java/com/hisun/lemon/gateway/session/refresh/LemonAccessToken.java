package com.hisun.lemon.gateway.session.refresh;

import java.io.Serializable;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 上午11:40:38
 *
 */
public class LemonAccessToken implements AccessToken, Serializable {
    private static final long serialVersionUID = -1658867699790775874L;
    
    private String value;
    private RefreshToken refreshToken;
    
    public LemonAccessToken(String value) {
        this.value = value;
        if(JudgeUtils.isBlank(this.value)) {
            LemonException.throwLemonException(ErrorMsgCode.SYS_ERROR.getMsgCd(), "the access token is blank.");
        }
    }
    @Override
    public String getValue() {
        return this.value;
    }
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
