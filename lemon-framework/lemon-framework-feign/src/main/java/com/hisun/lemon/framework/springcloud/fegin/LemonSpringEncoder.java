package com.hisun.lemon.framework.springcloud.fegin;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.validation.Validator;
import javax.ws.rs.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract.ConvertingExpander;
import org.springframework.core.convert.ConversionService;

import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.utils.IdGenUtils;
import com.hisun.lemon.framework.utils.ValidatorUtils;
import com.hisun.lemon.framework.validation.ClientValidated;

import feign.Param;
import feign.RequestTemplate;
import feign.codec.EncodeException;

/**
 * lemon扩展fegin encoder
 * 扩展 client bean validation
 * @author yuzhou
 * @date 2017年7月4日
 * @time 上午11:46:31
 *
 */
public class LemonSpringEncoder extends SpringEncoder {
    private static final Logger logger = LoggerFactory.getLogger(LemonSpringEncoder.class);
    
    private Validator validator;
    private boolean enabledValidate;
    private Param.Expander expander;
    private LemonBodyParameterResolver lemonBodyParameterResolver = LemonBodyParameterResolver.getInstance();
    
    public LemonSpringEncoder(
            ObjectFactory<HttpMessageConverters> messageConverters , ConversionService conversionService,
            Validator validator, boolean enabledValidate) {
        super(messageConverters);
        this.validator = validator;
        this.enabledValidate = enabledValidate;
        this.expander = new ConvertingExpander(conversionService);
    }
    
    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request)
            throws EncodeException {
        boolean continueEncode = true;
        if(null != requestBody && requestBody instanceof GenericDTO) {
            continueEncode = encodeGenericDTO((GenericDTO<?>) requestBody, request);
        }
        if(continueEncode) {
            super.encode(requestBody, bodyType, request);
            if(logger.isInfoEnabled()) {
                logger.info("Sending msg {}",request);
            }
        }
    }
    
    protected boolean encodeGenericDTO(GenericDTO<?> genericDTO, RequestTemplate request) {
        copyLemonDataToGenericDTOAndUpdateMsgId(genericDTO);
        if(enabledValidate) {
            clientValidated(genericDTO);
        }
        //GET请求发送GenericDTO对象采用parameters
        Collection<String> lemonParameters = request.headers().get(LemonBodyParameterProcessor.LEMON_BODY_HEADER_KEY);
        if(JudgeUtils.isNotEmpty(lemonParameters) && JudgeUtils.equals(request.method().toUpperCase(), HttpMethod.GET.toString())) {
            encodeLemonBody(genericDTO, lemonParameters, request);
            Map<String, Collection<String>> headers = request.headers();
            request.headers(null);
            headers.entrySet().stream().filter(e -> JudgeUtils.notEquals(LemonBodyParameterProcessor.LEMON_BODY_HEADER_KEY, e.getKey()))
                .forEach(p -> request.header(p.getKey(), p.getValue()));
            
            if(logger.isInfoEnabled()) {
                logger.info("Sending msg {}", request);
            }
            return false;
        }
        return true;
    }
    
    protected void copyLemonDataToGenericDTOAndUpdateMsgId(GenericDTO<?> genericDTO) {
        DataHelper.copyLemonDataToGenericDTOAndUpdateMsgId(LemonHolder.getLemonData(), genericDTO, IdGenUtils.generateMsgId());
        if(logger.isDebugEnabled()) {
            logger.debug("Copying lemon data {} to genericDTO {} and updating msgId in LemonSpringEncoder.",
                LemonHolder.getLemonData(), genericDTO );
        }
    }
    
    protected void clientValidated(GenericDTO<?> genericDTO) {
        //TODO @ClientValidated 为null时需要优化
        ClientValidated validated = AnnotationUtils.findAnnotation(genericDTO.getClass(), ClientValidated.class);
        if( JudgeUtils.isNotNull(validated)) {
            Class<?>[] groups = validated.value();
            ValidatorUtils.validateWithException(validator, genericDTO, groups);
        }
        //body object validate
        if(JudgeUtils.isNotNull(genericDTO.getBody())) {
            validated = AnnotationUtils.findAnnotation(genericDTO.getBody().getClass(), ClientValidated.class);
            if(JudgeUtils.isNotNull(validated)) {
                Class<?>[] groups = validated.value();
                ValidatorUtils.validateWithException(validator, genericDTO.getBody(), groups);
            }
        }
    }

    private void encodeLemonBody(GenericDTO<?> genericDTO, Collection<String> lemonParameters, RequestTemplate request) {
        Class<?> clazz = genericDTO.getClass();
        lemonParameters.stream().forEach(p -> { encodeLemonParameter(genericDTO, clazz, p, request); });
    }
    
    private void encodeLemonParameter(GenericDTO<?> genericDTO, Class<?> clazz, String parameter, RequestTemplate request) {
        Object paramVal = lemonBodyParameterResolver.readParameterValue(genericDTO, clazz, parameter);
        if(JudgeUtils.isNotNull(paramVal)) {
            request.query(false, parameter, expander.expand(paramVal));
        }
    }

}
