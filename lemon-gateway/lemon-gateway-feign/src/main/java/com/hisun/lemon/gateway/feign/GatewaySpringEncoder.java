package com.hisun.lemon.gateway.feign;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.core.convert.ConversionService;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.extension.Inject;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.springcloud.fegin.LemonSpringEncoder;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.ChannelFetcher;

import feign.RequestTemplate;
import feign.codec.EncodeException;

/**
 * @author yuzhou
 * @date 2017年9月11日
 * @time 下午5:44:08
 *
 */
public class GatewaySpringEncoder extends LemonSpringEncoder{

    @Inject(type=com.hisun.lemon.common.extension.Inject.Type.ACTIVATE)
    private ChannelFetcher channelFetcher;
    
    public GatewaySpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters, ConversionService conversionService,
        Validator validator, boolean enabledValidate) {
        super(messageConverters, conversionService, validator, enabledValidate);
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        HttpServletRequest httpRequest = GatewayHelper.getHttpServletRequest();
        request.header(LemonConstants.HTTP_HEADER_REQUESTID, GatewayHelper.getRequestId(httpRequest));
        request.header(LemonConstants.HTTP_HEADER_SOURCE, LemonUtils.getApplicationName());
        request.header(LemonConstants.HTTP_HEADER_LOCALE, Optional.ofNullable(LemonUtils.getLocale()).map(Locale::toString).orElseGet(() -> GatewayHelper.resolveLocale(httpRequest).toString()));
        request.header(LemonConstants.HTTP_HEADER_FOR, GatewayHelper.xforwardedFor());
        request.header(LemonConstants.HTTP_HEADER_CHANNL, Optional.ofNullable(channelFetcher).map(c -> c.fetchChannel()).orElse(null) );
        request.header(LemonConstants.HTTP_HEADER_TOKEN, GatewayHelper.getToken(httpRequest));
        super.encode(requestBody, bodyType, request);
    }
    
    @Override
    protected void copyLemonDataToGenericDTOAndUpdateMsgId(GenericDTO<?> genericDTO){}
    
}
