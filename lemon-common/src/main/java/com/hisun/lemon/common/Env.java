package com.hisun.lemon.common;

/**
 * 支持的环境
 * @author yuzhou
 * @date 2017年6月29日
 * @time 下午8:35:24
 *
 */
public enum Env {
    DEV, CI, SIT, UAT, STR, PRE, PRD;
    
    public static Env getDefaultEnv() {
        return Env.PRD;
    }
    
    public static String getProfile(Env env) {
        return env.toString().toLowerCase();
    }
    
    /**
     * 是否生产环境
     * @param env
     * @return
     */
    public static boolean isPrd(Env env) {
        if(PRD.equals(env)) {
            return true;
        }
        return false;
    }
    
}
