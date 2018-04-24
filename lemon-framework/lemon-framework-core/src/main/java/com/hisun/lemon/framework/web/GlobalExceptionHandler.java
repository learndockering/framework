package com.hisun.lemon.framework.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.service.IMsgInfoProcessor;
import com.hisun.lemon.framework.utils.ErrorMsgCdResolverHelper;
import com.hisun.lemon.framework.utils.BeanValidationInvalidHelper;

/**
 * 全局异常处理
 * @author yuzhou
 * @date 2017年6月20日
 * @time 下午6:05:02
 *
 *@ResponseStatus(HttpStatus.UNAUTHORIZED) 
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private IMsgInfoProcessor msgInfoProcessor;
    /**
     * 404 异常
     * @param nhfe
     * @param request
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public GenericRspDTO<?> handNoHandlerFoundException(NoHandlerFoundException nhfe, HttpServletRequest request) {
        if(logger.isErrorEnabled()) {
            logger.error("There is unexpected error (type=Not Found, status=404), request uri {}.", request.getRequestURI());
        }
        return processResponseDTO(GenericRspDTO.newInstance(ErrorMsgCode.NO_HANDLER_FOUND_ERROR.getMsgCd(), 
                "There is unexpected error (type=Not Found, status=404)."), request);
    }
    
    /**
     * 框架异常处理，一般业务抛出LemonException不会进到该方法
     * @param se
     * @param request
     * @return
     */
    @ExceptionHandler(LemonException.class)
    @ResponseBody
    public GenericRspDTO<?> handLemonException(LemonException se, HttpServletRequest request) {
        if(logger.isErrorEnabled()) {
            logger.error("Unexpected service exception occur at requesting uri {}. nestException ==>> {}", request.getRequestURI(), se);
        }
        return processResponseDTO(GenericRspDTO.newInstance(StringUtils.getDefaultIfEmpty(se.getMsgCd(), 
            ErrorMsgCdResolverHelper.resolveErrorMsgCd(se))), request);
    }
    
    /**
     * 在进入ControllerAspect前bean validation就已经执行了，所以bean validation校验错误会抛出该异常
     * 
     * @param error
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GenericRspDTO<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException error, HttpServletRequest request) {
        List<FieldError> errors = error.getBindingResult().getFieldErrors();
        if(null != errors) {
            BeanValidationInvalidHelper.resolveBeanValidationInvalidIfNecessary(error);
            //只显示第一条
            FieldError fieldError = errors.get(0);
            return processResponseDTO(GenericRspDTO.newInstance(StringUtils.getDefaultIfEmpty(fieldError.getDefaultMessage(), 
                    ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd())), request);
        }
        return processResponseDTO(GenericRspDTO.newInstance(ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd()), request);
    }
    
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public GenericRspDTO<?> handleBindException(BindException error, HttpServletRequest request) {
        List<FieldError> errors = error.getBindingResult().getFieldErrors();
        if(null != errors) {
            BeanValidationInvalidHelper.resolveBeanValidationInvalidIfNecessary(error);
            //只显示第一条
            FieldError fieldError = errors.get(0);
            return processResponseDTO(GenericRspDTO.newInstance(StringUtils.getDefaultIfEmpty(fieldError.getDefaultMessage(), 
                    ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd())), request);
        }
        return processResponseDTO(GenericRspDTO.newInstance(ErrorMsgCode.BEAN_VALIDATION_ERROR.getMsgCd()), request);
    }
    
    /**
     * 其他异常处理
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public GenericRspDTO<?> handException(Exception e, HttpServletRequest request) {
        if(logger.isErrorEnabled()) {
            logger.error("Unexpected exception occur at requesting uri {}. nestException ==>> {}", request.getRequestURI(), e);
        }
        return processResponseDTO(GenericRspDTO.newInstance(ErrorMsgCdResolverHelper.resolveErrorMsgCd(e)), request);
    }
    
    /**
     * 加工响应结果
     * TODO  取Locale 方式比较粗暴，如果是前端请求，Locale 以http head 为准
     * @param genericDTO
     */
    private <T> GenericRspDTO<T> processResponseDTO(GenericRspDTO<T> genericDTO, HttpServletRequest request) {
        if(JudgeUtils.isBlank(genericDTO.getRequestId()) && JudgeUtils.isNotNull(request) && JudgeUtils.isNotBlank(request.getHeader(LemonConstants.HTTP_HEADER_REQUESTID))) {
            DataHelper.setRequestId(genericDTO, request.getHeader(LemonConstants.HTTP_HEADER_REQUESTID));
        }
        if(JudgeUtils.isNotNull(this.msgInfoProcessor)) {
            return msgInfoProcessor.processMsgInfo(genericDTO);
        }
        if(logger.isWarnEnabled()) {
            logger.warn("Impl class for interface \"IMsgInfoProcessor\" is null.");
        }
        return genericDTO;
    }
}
