package com.hisun.lemon.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.cache.jcache.JCacheCacheable;
import com.hisun.lemon.framework.dao.IMsgInfoDao;
import com.hisun.lemon.framework.datasource.TargetDataSource;
import com.hisun.lemon.framework.entity.MsgInfoDO;
import com.hisun.lemon.framework.service.IMsgInfoService;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 消息码信息服务
 * @author yuzhou
 * @date 2017年6月27日
 * @time 下午1:37:39
 *
 */
@Transactional
@Service
public class MsgInfoServiceImpl implements IMsgInfoService {
    @Resource
    private IMsgInfoDao msgInfoDao;
    
    //默认语言
    private transient String defaultLanguage;
    
    @JCacheCacheable("lemonMsgInfo")
    @Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
    @TargetDataSource("lemon")
    public List<MsgInfoDO> findMsgInfos(String msgCd, String language) {
        if(JudgeUtils.isBlank(msgCd)) {
            return null;
        }
        if(JudgeUtils.isBlank(language)) {
            if(JudgeUtils.isBlank(this.defaultLanguage)){
                this.defaultLanguage = LemonUtils.getLemonEnvironment().getDefaultLocale().getLanguage();
            }
            language = this.defaultLanguage;
        }
        return this.msgInfoDao.findMsgInfos(msgCd, language);
    }
}
