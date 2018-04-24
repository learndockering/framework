package com.hisun.lemon.gateway.common.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.data.NoBody;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.gateway.common.GatewayAccessLogger;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.LemonObjectMapper;

/**
 * 响应消息解决
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午1:09:02
 *
 */
public class GatewayResourceResponseMessageResolver implements ResponseMessageResolver {
    private static final Logger logger = LoggerFactory.getLogger(GatewayResourceResponseMessageResolver.class);
    
    private LemonObjectMapper objectMapper;
    private MessageSource messageSource;
    
    public GatewayResourceResponseMessageResolver(ObjectMapper objectMapper, MessageSource messageSource) {
        this.objectMapper = new LemonObjectMapper(objectMapper);
        this.messageSource = messageSource;
    }
    
    @Override
    public void resolve(HttpServletRequest request, HttpServletResponse response, ErrorMsgCode errorMsgCode) throws IOException {
        resolve(request, response, errorMsgCode.getMsgCd());
    }

    @Override
    public void resolve(HttpServletRequest request, HttpServletResponse response, String msgCode) throws IOException {
        GenericRspDTO<NoBody> genericRspDTO = GenericRspDTO.newInstance(msgCode,
            this.messageSource.getMessage(msgCode, null, LemonUtils.getLocale()));
        resolve(request, response, genericRspDTO);
    }

    @Override
    public void resolve(HttpServletRequest request, HttpServletResponse response, GenericRspDTO<?> genericRspDTO)
            throws IOException {
        DataHelper.setRequestId(genericRspDTO, GatewayHelper.getRequestId(request));
        GatewayAccessLogger.printResponseLog(request, response, genericRspDTO, this.objectMapper);
        PrintWriter writer = response.getWriter();
        this.objectMapper.writeValue(writer, DataHelper.cleanRspGenericDTO(genericRspDTO));
        writer.flush();
    }

    @Override
    public byte[] generateBytes(ErrorMsgCode errorMsgCode) throws IOException {
        return generateBytes(errorMsgCode.getMsgCd());
    }

    @Override
    public byte[] generateBytes(String msgCode) throws IOException {
        return this.objectMapper.writeValueAsBytes(generateRspGenericDTO(msgCode));
    }

    @Override
    public String generateString(ErrorMsgCode errorMsgCode){
        return generateString(errorMsgCode.getMsgCd());
    }

    @Override
    public String generateString(String msgCode) {
        try {
            return objectMapper.writeValueAsString(generateRspGenericDTO(msgCode));
        } catch (JsonProcessingException e) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to parse RspGenericDTO to json, original msgCode is {}", msgCode);
            }
            throw LemonException.create(e);
        }
    }
    
    private GenericRspDTO<NoBody> generateRspGenericDTO(String msgCode) {
        String msgInfo = null;
        try{
            msgInfo = this.messageSource.getMessage(msgCode, null, LemonUtils.getLocale());
        } catch (NoSuchMessageException e) {
            msgInfo = this.messageSource.getMessage(ErrorMsgCode.SYS_ERROR.getMsgCd(), null, LemonUtils.getLocale());
        }
        GenericRspDTO<NoBody> genericRspDTO = GenericRspDTO.newInstance(msgCode, msgInfo);
        DataHelper.copyLemonDataToGenericDTO(LemonHolder.getLemonData(), genericRspDTO);
        return DataHelper.cleanRspGenericDTO(genericRspDTO);
    }

}
