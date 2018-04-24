package com.hisun.lemon.framework.aop;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.core.LemonDataInitializer;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.log.logback.MDCUtil;
import com.hisun.lemon.framework.service.IMsgInfoProcessor;
import com.hisun.lemon.framework.utils.BeanValidationInvalidHelper;
import com.hisun.lemon.framework.utils.ErrorMsgCdResolverHelper;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.framework.utils.ObjectMapperHelper;
import com.hisun.lemon.framework.utils.WebUtils;

/**
 * @author yuzhou
 * @date 2017年6月28日
 * @time 下午5:11:27
 *
 */
@Aspect
@Configuration
@Order(value=Ordered.HIGHEST_PRECEDENCE-1)
public class ControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
    
    private LemonDataInitializer webRequestLemonDataInitializer;
    private ObjectMapper objectMapper;
    private IMsgInfoProcessor msgInfoProcessor;
    
    @Value("${lemon.web.validateRequestHeader:true}")
    private boolean validateRequestHeader;
    
    public ControllerAspect(LemonDataInitializer webRequestLemonDataInitializer, ObjectMapper objectMapper, IMsgInfoProcessor msgInfoProcessor) {
        this.webRequestLemonDataInitializer = webRequestLemonDataInitializer;
        this.objectMapper = objectMapper;
        this.msgInfoProcessor = msgInfoProcessor;
    }
    
    @Pointcut("execution (* com.hisun..*Controller.*(..))")
    public void anyhisunControllerMethod(){}
    
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void anyRestControllerMethod(){}
    
    @Around("anyhisunControllerMethod()||anyRestControllerMethod()")
    public Object doAroundController(ProceedingJoinPoint pjp) throws Throwable {
        Instant startInstant = Instant.now();
        Object responseObject = null;             //返回对象
        try {
            beforeProceed(pjp);
            MDCUtil.putMDCKey(LemonUtils.getRequestId());
            ServerAccessLogger.logRequest(pjp, this.objectMapper);
            responseObject = pjp.proceed();
        } catch(Throwable e) {
            responseObject = throwableProceed(pjp, e);
        } finally {
            afterProceed(pjp, responseObject);
            ServerAccessLogger.logResponse(responseObject, this.objectMapper, DateTimeUtils.durationMillis(startInstant, Instant.now()));
        }
        return responseObject;
    }
    
    /**
     * controller 前处理
     * @param pjp
     */
    private void beforeProceed(ProceedingJoinPoint pjp) {
        List<GenericDTO<?>> requestGenericDTOs = Optional.ofNullable(pjp.getArgs()).filter(JudgeUtils::isNotEmpty).map(ControllerAspect.this::fetchGenericDTOFromArgs).orElse(null);
        HttpServletRequest request = WebUtils.getHttpServletRequest();
        if (JudgeUtils.isEmpty(requestGenericDTOs)) {
            LemonContext.setNeedProcessMsgInfo();
            initLemonData();
            if(logger.isWarnEnabled()) {
                logger.warn("The method \"{}\" not has \"GenericDTO\" type parameter in the controller class, such an approach is not recommended.", pjp.getSignature().getName());
            }
            return;
        }
        if(requestGenericDTOs.size() != 1) {
            LemonException.throwLemonException(ErrorMsgCode.SYS_ERROR.getMsgCd(), "The method can have only one \"GenericDTO\" parameter in controller class. illegal method is "+pjp.getSignature().getName());
        }
        GenericDTO<?> genericDTO = requestGenericDTOs.get(0);
        WebUtils.validateRequestData(genericDTO, request);
        if(isEntryTrade(genericDTO)) {
            LemonContext.setNeedProcessMsgInfo();
            JudgeUtils.callbackIfNecessary(validateRequestHeader, () -> {WebUtils.validateRequestHeaderForEntryTx(request);});
            initLemonData();
            copyLemonDataToGenericDTO(LemonHolder.getLemonData(), genericDTO);
            if(logger.isDebugEnabled()) {
                logger.debug("entry transaction, request id is {}", LemonUtils.getRequestId());
            }
            return;
        }
        DataHelper.setLemonDataAndAppendRouteInfo(genericDTO);
        DataHelper.setRouteInfo(genericDTO, LemonHolder.getLemonData().getRouteInfo());
    }
    
    /**
     * 异常处理
     * @param pjp
     * @param throwable
     * @return
     */
    private GenericRspDTO<?> throwableProceed(ProceedingJoinPoint pjp, Throwable throwable) {
        String msgCd = ErrorMsgCdResolverHelper.resolveErrorMsgCd(throwable);
        GenericRspDTO<?> genericRspDTO = GenericRspDTO.newInstance(msgCd);
        if(logger.isErrorEnabled() && Optional.ofNullable(throwable).filter(LemonException::isLemonException).map(t -> (LemonException)t).map(l -> !l.isBusinessException()).orElse(true)) {
            logger.error("Unexpected error occur during executing method \"{}\", \"{}\" ", pjp.getSignature().getName(), msgCd);
            logger.error("", throwable);
        }
        BeanValidationInvalidHelper.resolveBeanValidationInvalidIfNecessary(throwable);
        return genericRspDTO;
    }
    
    /**
     * Controller 后处理
     * @param pjp
     * @param responseObject
     */
    private void afterProceed(ProceedingJoinPoint pjp, Object responseObject) {
        if(JudgeUtils.isNotNull(responseObject) && responseObject instanceof GenericRspDTO) {
            GenericRspDTO<?> genericRspDTO = (GenericRspDTO<?>) responseObject;
            checkGenericRspDTO(genericRspDTO);
            copyLemonDataToGenericRspDTO(LemonHolder.getLemonData(), genericRspDTO);
            processMsgInfo(genericRspDTO);
        }
    }
    
    private List<GenericDTO<?>> fetchGenericDTOFromArgs(Object[] args){
        return Stream.of(args).filter(o -> o instanceof GenericDTO).map(m -> (GenericDTO<?>)m).collect(Collectors.toList());
    }
    
    private void copyLemonDataToGenericDTO(LemonData lemonData, GenericDTO<?> genericDTO) {
        DataHelper.copyLemonDataToGenericDTO(lemonData, genericDTO);
    }
    
    private void copyLemonDataToGenericRspDTO(LemonData lemonData, GenericRspDTO<?> genericRspDTO) {
        if(JudgeUtils.isNotNull(lemonData) && JudgeUtils.isNotNull(genericRspDTO)) {
            DataHelper.copyLemonDataToGenericDTO(lemonData, genericRspDTO);
            DataHelper.cleanRspGenericDTO(genericRspDTO);
        }
    }

    /**
     * check returning GenericRspDTO
     * @param genericRspDTO
     */
    private void checkGenericRspDTO(GenericRspDTO<?> genericRspDTO) {
        if(JudgeUtils.isBlank(genericRspDTO.getMsgCd())) {
            genericRspDTO.setMsgCd(ErrorMsgCode.MSG_CD_NOT_EXISTS.getMsgCd());
            if(logger.isErrorEnabled()) {
                logger.error("The field value of msgCd is blank in response DTO {}, set msgCd to \"{}\" by lemon framework.", genericRspDTO, ErrorMsgCode.MSG_CD_NOT_EXISTS);
            }
        }
    }

    /**
     * processing msg info
     * @param rspDTO
     */
    private void processMsgInfo(GenericRspDTO<?> rspDTO) {
        if (LemonContext.needProcessMsgInfo()) {
            this.msgInfoProcessor.processMsgInfo(rspDTO);
        }
    }
    
    //入口交易
    private boolean isEntryTrade(GenericDTO<?> genericDTO) {
        if(JudgeUtils.isBlankAll(genericDTO.getRouteInfo()) || JudgeUtils.equalsAny(genericDTO.getRouteInfo(), LemonUtils.getGateways())) {
            return true;
        }
        return false;
    }
    
    private void initLemonData() {
        this.webRequestLemonDataInitializer.initLemonData();
    }
    
    public static class ServerAccessLogger {
        public static final Logger logger = LoggerFactory.getLogger(ServerAccessLogger.class);
        public static void logRequest(ProceedingJoinPoint pjp, ObjectMapper objectMapper) {
            if(logger.isInfoEnabled()) {
                try{
                    HttpServletRequest httpRequest = WebUtils.getHttpServletRequest();
                    String data = Optional.ofNullable(pjp.getArgs()).map(args -> Stream.of(args).map(arg -> toString(arg, objectMapper)).collect(Collectors.joining("~"))).orElse("");
                    logger.info("{\"action\":\"request\",\"uri\":\"{}\",\"remoteIp\":\"{}\",\"data\":{}}", httpRequest.getRequestURI(), LemonUtils.getClientIp(), data);
                } catch(Throwable t) {}
            }
        }
        
        public static void logResponse(Object result, ObjectMapper objectMapper, long execTm) {
            if(logger.isInfoEnabled()) {
                try{
                    logger.info("{\"action\":\"response\",\"execTm\":\"{}\",\"data\":{}}", execTm, Optional.ofNullable(result).map(r -> toString(r, objectMapper)).orElse(null));
                } catch (Throwable t) {}
            }
        }
        
        private static String toString(Object obj, ObjectMapper objectMapper) {
            return ObjectMapperHelper.writeValueAsString(objectMapper, obj, true);
        }
    }
    
}
