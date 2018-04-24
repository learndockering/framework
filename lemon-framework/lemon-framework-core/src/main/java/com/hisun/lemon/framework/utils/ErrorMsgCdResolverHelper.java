package com.hisun.lemon.framework.utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.exception.MsgCodeResolver;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import static com.hisun.lemon.common.utils.StringUtils.getDefaultIfEmpty;
import com.netflix.client.ClientException;

import feign.FeignException;
import feign.RetryableException;
import feign.codec.EncodeException;

/**
 * @author yuzhou
 * @date 2017年8月11日
 * @time 下午1:42:21
 *
 */
public class ErrorMsgCdResolverHelper {
    private static final Logger logger = LoggerFactory.getLogger(ErrorMsgCdResolverHelper.class);
    private static List<MsgCodeResolver> msgCodeResolvers = null;
    
    static {
        msgCodeResolvers = ExtensionLoader.getExtensionServices(MsgCodeResolver.class);
        if(logger.isInfoEnabled()) {
            logger.info("message code resolvers {}", msgCodeResolvers);
        }
    }
    /**
     * 获取错误码
     * @param e
     * @return
     */
    public static String resolveErrorMsgCd(Throwable e) {
        if (LemonException.isLemonException(e)) {
            LemonException le = (LemonException) e;
            String msgCd = Optional.ofNullable(le.getMsgCd()).orElseGet(null);
            if (JudgeUtils.isNotBlank(msgCd)) {
                return msgCd;
            }
            if(le.getCause() == null) {
                return ErrorMsgCode.SYS_ERROR.getMsgCd();
            }
            e = le.getCause();
        } 
        
        ErrorMsgCode errorMsgCode= resolveErrorMsgCode(e, 1);
        if(! ErrorMsgCode.CONTINUE_RESOLVE.equals(errorMsgCode)) {
            return errorMsgCode.getMsgCd();
        }
        
        if (e instanceof EncodeException) {
            if(null != e.getCause() && e.getCause() instanceof ConstraintViolationException) {
                return constraintViolationException((ConstraintViolationException) e.getCause());
            }
        }
        //bean validate
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException error = (MethodArgumentNotValidException) e;
            List<FieldError> errors = error.getBindingResult().getFieldErrors();
            if(JudgeUtils.isNotEmpty(errors)) {
                //只显示第一条
                FieldError fieldError = errors.get(0);
                return StringUtils.getDefaultIfEmpty(fieldError.getDefaultMessage(), ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd());
            }
            return ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd();
        }
        //bean validate
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) e;
            return constraintViolationException(cve);
        }
        if(e instanceof RetryableException) {
            if(isTimeOutException(e.getCause())) {
                return ErrorMsgCode.CLIENT_TIMEOUT.getMsgCd();
            }
            if(null != e.getCause() && e.getCause() instanceof UnknownHostException) {
                return ErrorMsgCode.CLIENT_EXCEPTION_UNKNOWN_HOST.getMsgCd();
            }
            return ErrorMsgCode.CLIENT_EXCEPTION.getMsgCd();
        }
        if(e instanceof FeignException) {
            FeignException fe = (FeignException) e;
            if(fe.status() == 404) {
                return ErrorMsgCode.SERVER_RESOURCE_NOT_FOUND.getMsgCd();
            }
            if(isTimeOutException(e.getCause())) {
                return ErrorMsgCode.CLIENT_TIMEOUT.getMsgCd();
            }
        }
        if (e.getCause() != null && e.getCause() instanceof ClientException) {
            if(isTimeOutException(e.getCause())) {
                return ErrorMsgCode.CLIENT_TIMEOUT.getMsgCd();
            }
            return ErrorMsgCode.CLIENT_EXCEPTION.getMsgCd();
        }
        return ErrorMsgCode.SYS_ERROR.getMsgCd();
    }
    
    private static ErrorMsgCode resolveErrorMsgCode(Throwable e, int count) {
        if(count >= 3) {
            return ErrorMsgCode.SYS_ERROR;
        }
        if(isBasicError(e.getClass())) {
            return ErrorMsgCode.SYS_ERROR;
        }
        ErrorMsgCode errorMsgCode = ErrorMsgCode.CONTINUE_RESOLVE;
        if(JudgeUtils.isEmpty(msgCodeResolvers)) return errorMsgCode;
        for(MsgCodeResolver resolver : msgCodeResolvers) {
            if (resolver.support(e)) {
                errorMsgCode = resolver.resolve(e);
                if (null == errorMsgCode) {
                    errorMsgCode = ErrorMsgCode.SYS_ERROR;
                    break;
                }
                if (ErrorMsgCode.CONTINUE_RESOLVE.equals(errorMsgCode)) {
                    e = e.getCause();
                    if (null == e) {
                        errorMsgCode = ErrorMsgCode.SYS_ERROR;
                        break;
                    }
                    errorMsgCode = resolveErrorMsgCode(e, count + 1);
                }
                break;
            }
        }
        return errorMsgCode;
    }
    
    private static boolean isBasicError(Class<?> clazz) {
        if(clazz.equals(Exception.class) || clazz.equals(Throwable.class) || clazz.equals(Error.class)
            || clazz.equals(RuntimeException.class)) {
            return true;
        }
        return false;
    }
    
    private static String constraintViolationException(ConstraintViolationException cve) {
        String errorMsgCd = null;
        if(JudgeUtils.isNotEmpty(cve.getConstraintViolations())) {
            ConstraintViolation<?> constraintViolation = cve.getConstraintViolations().iterator().next();
            errorMsgCd = constraintViolation.getMessage();
        }
        return getDefaultIfEmpty(errorMsgCd ,ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd());
    }
    
    private static boolean isTimeOutException(Throwable t) {
        if(t instanceof SocketTimeoutException) {
            return true;
        }
        Throwable t2 = t.getCause();
        if(null == t2) {
            return false;
        }
        int count = 0;
        while(null != t2 && count <= 4) {
            if(t2 instanceof SocketTimeoutException) {
                return true;
            }
            t2 = t2.getCause();
            count ++;
        }
        return false;
    }
}
