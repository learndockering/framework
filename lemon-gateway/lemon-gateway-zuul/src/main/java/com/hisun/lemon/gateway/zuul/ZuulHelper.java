package com.hisun.lemon.gateway.zuul;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.LemonObjectMapper;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties.ZuulRoute;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.netflix.zuul.context.RequestContext;

/**
 * @author yuzhou
 * @date 2017年8月7日
 * @time 下午3:04:49
 *
 */
public class ZuulHelper {
    private static final Logger logger = LoggerFactory.getLogger(ZuulHelper.class);
    
    public static final String CTX_CURRENT_REQUEST_URI = "LEMON_CURRENT_REQUEST_URI";
    public static final String CTX_CURRENT_ZUUL_ROUTE = "LEMON_CURRENT_ZUUL_ROUTE";
    public static final String CTX_CURRENT_PATH_MATCHERS = "LEMON_CURRENT_PATH_MATCHERS";
    public static final String CTX_CURRENT_BODY_STRING = "LEMON_CURRENT_BODY_STRING";
    
    public static final String IGNORE_SET_CHANNEL = GatewayHelper.IGNORE_SET_CHANNEL;
    
    private LemonObjectMapper objectMapper;
    private UrlPathHelper urlPathHelper;
    private RouteLocator routeLocator;
    private ZuulExtensionProperties zuulExtensionProperties;
    private ISecurityService securityServiceImpl;
    
    public ZuulHelper(LemonObjectMapper objectMapper, UrlPathHelper urlPathHelper, 
        RouteLocator routeLocator, ZuulExtensionProperties zuulExtensionProperties, ISecurityService securityServiceImpl) {
        this.objectMapper = objectMapper;
        this.urlPathHelper = urlPathHelper;
        this.routeLocator = routeLocator;
        this.zuulExtensionProperties = zuulExtensionProperties;
        this.securityServiceImpl = securityServiceImpl;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> getCurrentPathMatchers(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return Optional.ofNullable(ctx.get(CTX_CURRENT_PATH_MATCHERS)).map(m -> (Map<String, String>) m)
            .orElseGet(() ->{
                String requestURI = this.getCurrentRequestURI();
                ZuulRoute zuulRoute = this.getCurrentZuulRoute();
                AntPathMatcher matcher = new AntPathMatcher();
                Map<String, String> matchedPaths = matcher.extractUriTemplateVariables(zuulRoute.getPath(), requestURI);
                ctx.set(CTX_CURRENT_PATH_MATCHERS, matchedPaths);
                return matchedPaths;
            });
        
    }
    
    public String getCurrentRequestURI() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = Optional.ofNullable(ctx.get(CTX_CURRENT_REQUEST_URI)).map(m -> (String)m)
            .orElseGet(() ->{
                HttpServletRequest request = Optional.ofNullable(ctx.getRequest()).orElseGet(() -> GatewayHelper.getHttpServletRequest());
                String requestURI0 = this.urlPathHelper.getPathWithinApplication(request);
                ctx.set(CTX_CURRENT_REQUEST_URI, requestURI0);
                return requestURI0;
            });
        return requestURI;
    }
    
    public ZuulRoute getZuulRouteByPath(String path) {
        Route route = this.routeLocator.getMatchingRoute(path);
        if(JudgeUtils.isNull(route)) {
            return null;
        }
        ZuulRoute zuulRoute = this.zuulExtensionProperties.getZuulRoute(route.getId());
        return zuulRoute;
    }
    
    public ZuulRoute getCurrentZuulRoute() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return Optional.ofNullable(ctx.get(CTX_CURRENT_ZUUL_ROUTE)).map(m -> (ZuulRoute)m)
            .orElseGet(() -> {
                ZuulRoute zuulRoute = this.getZuulRouteByPath(this.getCurrentRequestURI());
                ctx.set(CTX_CURRENT_ZUUL_ROUTE, zuulRoute);
                return zuulRoute;
            });
    }
    
    public String getCurrentRequestBody() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return Optional.ofNullable(ctx.get(CTX_CURRENT_BODY_STRING)).map(m -> (String)m)
            .orElseGet(() -> {
                HttpServletRequest request = Optional.ofNullable(ctx.getRequest()).orElse(GatewayHelper.getHttpServletRequest());
                String body = GatewayHelper.getRequestBody(request);
                ctx.set(CTX_CURRENT_BODY_STRING, body);
                return body;
            });
    }
    
    public String getQueryParameters(HttpServletRequest request) {
        return request.getQueryString();
    }
    
    public Map<String, String[]> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> maps = request.getParameterMap();
        return maps;
    }
    
    public HttpServletRequest getHttpServletRequest() {
        return Optional.ofNullable(RequestContext.getCurrentContext()).map(c -> c.getRequest()).orElse(GatewayHelper.getHttpServletRequest());
    }
    
    public SecurityBO findSecurityBO(HttpServletRequest request) {
        return Optional.ofNullable(GatewayHelper.getSecureIndex(request)).filter(StringUtils::isNotBlank).map(i -> findSecurityBO(i)).orElse(null);
    }
    
    public SecurityBO findSecurityBO(String secureIndex) {
        return this.securityServiceImpl.querySecurity(secureIndex);
    }
    
    public SecurityBO findMerchantSecurityBO(String merchantNo, String interfaceName, String interfaceVersion) {
        return null;
    }
    
    /**
     * 输入参数检查
     * @param request
     * @return
     */
    public boolean validateInputParams(HttpServletRequest request) {
        String[] sensitiveHeaders = LemonConstants.SENSTIVE_HEADER_NAMES;
        if(JudgeUtils.isNotEmpty(sensitiveHeaders)) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while(headerNames.hasMoreElements()) {
                String headName = headerNames.nextElement();
                if(StringUtils.isNotBlank(headName) && JudgeUtils.contain(sensitiveHeaders, headName)) {
                    if(logger.isErrorEnabled()) {
                        logger.error("illegal http request header {} exists.", headName);
                    }
                    return false;
                }
            }
        }
        
        String[] sensitiveParameters = LemonConstants.SENSTIVE_PARAMETER_NAMES;
        if(JudgeUtils.isNotEmpty(sensitiveParameters)) {
            Map<String, String[]> parameterMap = this.getParameterMap(request);
            if(JudgeUtils.isNotEmpty(parameterMap)) {
                for(String paramKey : parameterMap.keySet()) {
                    if(JudgeUtils.isNotBlank(paramKey) && JudgeUtils.contain(sensitiveParameters, paramKey)) {
                        if(logger.isErrorEnabled()) {
                            logger.error("illegal param {} exists.", paramKey);
                        }
                        return false;
                    }
                }
            }
            
            InputStream inputStream = GatewayHelper.getRequestInputStream(request);
            if(JudgeUtils.available(inputStream)) {
                Map<?,?> dto = null;
                try{
                    dto = this.objectMapper.readValue(inputStream, Map.class);
                } catch(Exception e) {
                    if(logger.isErrorEnabled()) {
                        logger.error("failed to validate input stream. ", e);
                    }
                }
                if(JudgeUtils.isNotEmpty(dto)) {
                    for (Object paramKeyObj : dto.keySet()) {
                        String paramKey = String.valueOf(paramKeyObj);
                        if(JudgeUtils.isNotBlank(paramKey) && JudgeUtils.contain(sensitiveParameters, paramKey)) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
}
