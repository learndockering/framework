package com.hisun.lemon.gateway.zuul.signature;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

import com.hisun.lemon.common.HttpMethod;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties.ZuulRoute;
import com.hisun.lemon.gateway.common.signature.AbstractSignatureResolver;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.netflix.zuul.context.RequestContext;

/**
 * @author yuzhou
 * @date 2017年10月11日
 * @time 下午3:44:39
 *
 */
public abstract class AbstractZuulSignatureResolver extends AbstractSignatureResolver {
    
    protected static final String REQUEST_HEADER_SIGN = "x-lemon-sign";
    protected static final String SIGNATURED_PATHS_SEPARATOR = ",";
    protected static final String ZUUL_CTX_ALREADY_SIGNATURED = "ZUUL_CTX_ALREADY_SIGNATURED";
    
    protected ZuulHelper zuulHelper;
    
    public AbstractZuulSignatureResolver(ZuulHelper zuulHelper) {
        this.zuulHelper = zuulHelper;
    }
    
    @Override
    protected boolean alreadySignatured() {
        return JudgeUtils.isTrue(RequestContext.getCurrentContext().getBoolean(ZUUL_CTX_ALREADY_SIGNATURED), false);
    }
    
    @Override
    protected void setAlreadySignatured() {
        RequestContext.getCurrentContext().set(ZUUL_CTX_ALREADY_SIGNATURED);
    }
    
    protected String getSignedString(HttpServletRequest request) {
        return request.getHeader(REQUEST_HEADER_SIGN);
    }
    
    protected String getVerifyString(HttpServletRequest request) {
        String signStr = null;
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod().toUpperCase());
        switch (httpMethod) {
        case GET:
            signStr = this.zuulHelper.getQueryParameters(request);
            if (JudgeUtils.isNotBlank(signStr)) {
                return signStr;
            }
            return getPathVariable(request);
            
        default:
            signStr = this.zuulHelper.getCurrentRequestBody();
            if(JudgeUtils.isNotBlank(signStr)) {
                return signStr;
            }
            if(isFormRequest(request)) {
                signStr = this.getFormData(request);
                if(JudgeUtils.isNotBlank(signStr)) {
                    return signStr;
                }
            }
            return getPathVariable(request);
        }
    }
    
    protected boolean isFormRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        MediaType mediaType = MediaType.valueOf(contentType);
        return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)
                || MediaType.MULTIPART_FORM_DATA.includes(mediaType);
    }
    
    private String getPathVariable(HttpServletRequest request) {
        String requestURI = this.zuulHelper.getCurrentRequestURI();
        if (JudgeUtils.isBlank(requestURI)) {
            return null;
        }
        
        ZuulRoute zuulRoute = this.zuulHelper.getCurrentZuulRoute();
        if(JudgeUtils.isNull(zuulRoute) || JudgeUtils.isBlank(zuulRoute.getPath())
            || zuulRoute.getPath().indexOf("{") == -1) {
            return null;
        }
        
        Map<String, String> matchedPaths = this.zuulHelper.getCurrentPathMatchers();
        if(JudgeUtils.isEmpty(matchedPaths)) {
            return null;
        }
        String signaturedPaths = zuulRoute.getSignaturedParameters();
        if(JudgeUtils.isNotBlank(signaturedPaths)) {
            List<String> signaturedPathList = Arrays.asList(signaturedPaths.split(SIGNATURED_PATHS_SEPARATOR));
            return matchedPaths.values().stream().filter(m -> signaturedPathList.contains(m)).collect(Collectors.joining());
        }
        return matchedPaths.values().stream().collect(Collectors.joining());
    }
    
    protected String getFormData(HttpServletRequest request) {
        Map<String, String[]> parameterMap = this.zuulHelper.getParameterMap(request);
        if(JudgeUtils.isEmpty(parameterMap)) {
            return null;
        }
        ZuulRoute zuulRoute = this.zuulHelper.getCurrentZuulRoute();
        if(JudgeUtils.isNull(zuulRoute)) {
            return null;
        }
        String signaturedParameters = zuulRoute.getSignaturedParameters();
        if(JudgeUtils.isBlank(signaturedParameters)) {
            if(logger.isDebugEnabled()) {
                logger.debug("Can not fetch sign data form form because of no signatured parameter defined in zuul route configuration ");
            }
            return null;
        }
        StringBuilder sb  = new StringBuilder();
        Stream.of(signaturedParameters.split(SIGNATURED_PATHS_SEPARATOR)).forEachOrdered( s -> {
            String[] vs = parameterMap.get(s);
            if(JudgeUtils.isNotEmpty(vs)) {
                Stream.of(vs).forEachOrdered(sb::append);
            }
        });
        return sb.toString();
    }

}
