package com.hisun.lemon.framework.web.bind.generic;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.annotation.LemonBody;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericDTOFields;

/**
 * 解决GET传GenericDTO的问题；
 * 因FeignGET请求用Json传对象，这样服务端必须要用@RequestBody注解，但服务端有可能需要其他客户端用param GET请求，与用@RequestBody注解冲突
 * 现GET请求传GenericDTO必须用@LemonBody注解，解决上述冲突
 *
 * @see com.hisun.lemon.framework.annotation.LemonBody
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午5:51:54
 *
 */
public class LemonServletModelAttributeMethodProcessor implements HandlerMethodArgumentResolver{

    private ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor;
    
    public LemonServletModelAttributeMethodProcessor() {
        this.servletModelAttributeMethodProcessor = new ServletModelAttributeMethodProcessor(false);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LemonBody.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        LemonWebDataBinderFactory lemonWebDataBinderFactory = new LemonWebDataBinderFactory(binderFactory);
        Object object = this.servletModelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, lemonWebDataBinderFactory);
        if(object instanceof GenericDTO){
            GenericDTO<?> genericDTO = (GenericDTO<?>) object;
            ConversionService conversionService = lemonWebDataBinderFactory.getConversionService();
            ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
            Map<String, String[]> parameterMap =  servletRequest.getParameterMap();
            if(JudgeUtils.isNotEmpty(parameterMap)) {
                parameterMap.entrySet().stream().filter(e -> GenericDTOFields.isGenericDTOField(e.getKey()))
                    .forEach(e -> DataHelper.setGenericDTOPropertyValue(genericDTO, e.getKey(), convertIfNecessary(conversionService, e.getValue()[0], e.getKey()) ) );
            }
        }
        return object;
    }
    
    public Object convertIfNecessary(ConversionService conversionService, Object newValue, String fieldName) {
        return convertIfNecessary(conversionService, newValue, DataHelper.getPropertyTypeDescriptor(fieldName));
    }
    
    public Object convertIfNecessary(ConversionService conversionService, Object newValue, TypeDescriptor typeDescriptor) {
        if ( conversionService != null && newValue != null && typeDescriptor != null) {
            TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
            if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
                try {
                    return conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
                }
                catch (ConversionFailedException ex) {
                    throw LemonException.create(ex);
                }
            }
        }
        return newValue;
    }
    
    /**
     * 除了Lemon fields外其他属性的数据绑定
     * @author yuzhou
     * @date 2017年9月15日
     * @time 下午1:54:18
     *
     */
    public static class LemonWebDataBinderFactory implements WebDataBinderFactory {
        private WebDataBinderFactory webDateBinderFactory;
        private ConversionService conversionService;
        public LemonWebDataBinderFactory(WebDataBinderFactory webDateBinderFactory) {
            this.webDateBinderFactory = webDateBinderFactory;
        }
        
        @Override
        public WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
            WebDataBinder webDataBinder = webDateBinderFactory.createBinder(webRequest, target, objectName);
            webDataBinder.setDisallowedFields(GenericDTOFields.LEMON_FIELDS);
            conversionService = webDataBinder.getConversionService();
            return webDataBinder;
        }
        
        public ConversionService getConversionService() {
            return conversionService;
        }
        
    }

}
