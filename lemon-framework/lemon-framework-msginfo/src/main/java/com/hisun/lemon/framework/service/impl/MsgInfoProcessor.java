package com.hisun.lemon.framework.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.entity.MsgInfoDO;
import com.hisun.lemon.framework.service.IMsgInfoProcessor;
import com.hisun.lemon.framework.service.IMsgInfoService;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 消息码处理
 * @author yuzhou
 * @date 2017年7月19日
 * @time 下午2:23:57
 *
 */
public class MsgInfoProcessor implements IMsgInfoProcessor{
    private static final Logger logger = LoggerFactory.getLogger(MsgInfoProcessor.class);
    
    private static final String ALL_SCENARIO = "*";
    
    private IMsgInfoService msgInfoServiceImpl;
    
    public MsgInfoProcessor(IMsgInfoService msgInfoServiceImpl) {
        this.msgInfoServiceImpl = msgInfoServiceImpl;
    }
    
    /**
     * processing msg info
     * @param genericRspDTO
     */
    public <T> GenericRspDTO<T> processMsgInfo(GenericRspDTO<T> genericRspDTO) {
        if(JudgeUtils.isNotBlank(genericRspDTO.getMsgCd()) && JudgeUtils.isNotSuccess(genericRspDTO.getMsgCd())) {
            try{
                List<MsgInfoDO> msgInfos = this.msgInfoServiceImpl.findMsgInfos(genericRspDTO.getMsgCd(), LemonUtils.getLocale().getLanguage());
                if(JudgeUtils.isNotEmpty(msgInfos)) {
                    if(msgInfos.size() == 1) {
                        genericRspDTO.setMsgInfo(msgInfos.get(0).getMsgInfo());
                    } else {
                        String busi = LemonUtils.getBusiness();
                        String msgInfo = null;
                        String defaultMsgInfo = null;
                        if (JudgeUtils.isNotBlank(busi)) {
                            for(MsgInfoDO busiInfoDO : msgInfos) {
                                if(JudgeUtils.equals(ALL_SCENARIO, busiInfoDO.getScenario())) {
                                    defaultMsgInfo = busiInfoDO.getMsgInfo();
                                }
                                if(JudgeUtils.equals(busi, busiInfoDO.getScenario())) {
                                    msgInfo = busiInfoDO.getMsgInfo();
                                    if(JudgeUtils.isNotBlank(msgInfo)) {
                                        break;
                                    }
                                }
                            }
                            if(JudgeUtils.isBlank(msgInfo)) {
                                msgInfo = defaultMsgInfo;
                            }
                        } else {
                            for(MsgInfoDO busiInfoDO : msgInfos) {
                                if(JudgeUtils.equals(ALL_SCENARIO, busiInfoDO.getScenario())) {
                                    defaultMsgInfo = busiInfoDO.getMsgInfo();
                                    msgInfo = busiInfoDO.getMsgInfo();
                                    break;
                                }
                            }
                        }
                        genericRspDTO.setMsgInfo(msgInfo);
                    }
                }
            } catch(Exception e) {
                if(logger.isErrorEnabled()) {
                    logger.error("Processing msgInfo occur error, ", e);
                }
            }
        }
        return genericRspDTO;
    }

}
