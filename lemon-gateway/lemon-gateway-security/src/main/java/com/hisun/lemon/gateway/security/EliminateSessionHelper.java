package com.hisun.lemon.gateway.security;


/**
 * 过期session
 * @author yuzhou
 * @date 2017年9月15日
 * @time 下午5:25:08
 *
 */
public class EliminateSessionHelper {

    /**
     * 剔除多终端用户session维度
     * 商户以loginName维度控制，因商户一个userId对用多个loginName
     * 其他的以userId维度控制
     * @param principal
     * @return
     */
    public static String eliminateSessionDimension(Object principal) {
        Channel loginChannel = SecurityUtils.getLoginChannelByPrincipal(principal);
        switch (loginChannel) {
        case MER:
            return SecurityUtils.getLoginUser().getLoginName();
        default:
            return SecurityUtils.getLoginUser().getUserId();
        }
    }
    
  
}
