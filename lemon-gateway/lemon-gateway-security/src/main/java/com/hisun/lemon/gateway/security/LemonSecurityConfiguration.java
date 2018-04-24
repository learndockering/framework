package com.hisun.lemon.gateway.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayConstants;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties.ZuulRoute;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;
import com.hisun.lemon.gateway.session.refresh.LemonRefreshTokenAuthenticationProcessingFilter;
import com.hisun.lemon.gateway.session.refresh.RefreshTokenAuthenticationProvider;

/**
 * spring security 配置
 * @author yuzhou
 * @date 2017年7月26日
 * @time 上午10:39:23
 *
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({ ZuulExtensionProperties.class })
public class LemonSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LemonSecurityConfiguration.class);
    public static final String LOGIN_PATH = "/security/login";
    public static final String LOGOUT_PATH = "/security/logout";
    public static final String REFRESH_PAHT = LemonRefreshTokenAuthenticationProcessingFilter.DEFAULT_REFRESH_TOKEN_PROCESSES_URL;
    public static final String USERNAME = "loginName";
    public static final String PASSWORD = "loginPwd";
    
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    @Qualifier("lemonAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authSuccessHandler;
    
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private LemonAuthenticationProvider lemonAuthenticationProvider;
    @Autowired
    private RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;
    @Autowired
    protected ZuulExtensionProperties zuulExtendProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionInformationExpiredStrategy lemonSessionInformationExpiredStrategy;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;
    @Autowired
    private LogoutHandler lemonSaveSessionIdLogoutHandler;
    
    private SignatureResolver signatureResolver;
    
    private String[] anonymousAntPatterns = null;
    
    public LemonSecurityConfiguration(SignatureResolver signatureResolver) {
        this.signatureResolver = signatureResolver;
    }
    
    @PostConstruct
    public void init() {
         List<String> unauthencatedPaths = zuulExtendProperties.getRoutes().values().stream().filter(r 
            -> JudgeUtils.not(r.getAuthenticated())).map(ZuulRoute::getPath)
            .collect(Collectors.toList());
         if (unauthencatedPaths != null) {
             anonymousAntPatterns = unauthencatedPaths.toArray(new String[unauthencatedPaths.size()]);
         } else {
             unauthencatedPaths = new ArrayList<>();
             unauthencatedPaths.add(REFRESH_PAHT);
             anonymousAntPatterns = new String[]{};
         }
    }
    
    @Bean
    public SignatureResolver loginSignatureResolver() {
        return new SignatureResolver (){
            public boolean shouldVerify() {
                return true;
            }
            public boolean verify() {
                LemonContext.putToCurrentContext(GatewayConstants.REQUIRE_CLEAN_ZUUL_CONTEXT, Boolean.TRUE);
                if (JudgeUtils.isNotNull(signatureResolver)) {
                    return signatureResolver.verify();
                }
                if(logger.isWarnEnabled()) {
                    logger.warn("Bean \"signatureResolver\" do not defined in spring applicaiton. login verify failure.");
                }
                return false;
            }
        };
    }
    
    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        return new ConcurrentSessionFilter(sessionRegistry, lemonSessionInformationExpiredStrategy);
    }
    
    @Bean
    public LemonAuthenticationFilter lemonAuthenticationFilter() {
        LemonAuthenticationFilter laf =  new LemonAuthenticationFilter(this.objectMapper, loginSignatureResolver());
        try {
            laf.setAuthenticationManager(authenticationManager());
        } catch (Exception e) {
            LemonException.throwLemonException(e);
        }
        laf.setAuthenticationSuccessHandler(this.authSuccessHandler);
        laf.setAuthenticationFailureHandler(this.authFailureHandler);
        laf.setFilterProcessesUrl(LOGIN_PATH);
        laf.setUsernameParameter(USERNAME);
        laf.setPasswordParameter(PASSWORD);
        laf.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        return laf;
    }
    
    @Qualifier("nonRefreshTokenLemonAuthenticationSuccessHandler") 
    @Autowired
    private LemonAuthenticationSuccessHandler nonRefreshTokenLemonAuthenticationSuccessHandler;
    
    @Bean
    public LemonRefreshTokenAuthenticationProcessingFilter lemonRefreshTokenAuthenticationProcessingFilter() {
        LemonRefreshTokenAuthenticationProcessingFilter lrtapf =  new LemonRefreshTokenAuthenticationProcessingFilter(this.objectMapper, loginSignatureResolver());
        try {
            lrtapf.setAuthenticationManager(authenticationManager());
        } catch (Exception e) {
            LemonException.throwLemonException(e);
        }
        lrtapf.setAuthenticationSuccessHandler(nonRefreshTokenLemonAuthenticationSuccessHandler);
        lrtapf.setAuthenticationFailureHandler(this.authFailureHandler);
        lrtapf.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        return lrtapf;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(logger.isInfoEnabled()) {
            logger.info("Anonymous access urls ==> {} <==.", Stream.of(anonymousAntPatterns).collect(Collectors.joining(",")));
        }
        http.authorizeRequests()
            .antMatchers(anonymousAntPatterns).permitAll()
            .anyRequest().authenticated()
        .and()
            .authenticationProvider(lemonAuthenticationProvider)
            .authenticationProvider(refreshTokenAuthenticationProvider)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        /*.and()
            .formLogin().permitAll()
            .loginProcessingUrl(LOGIN_PATH)
            .usernameParameter(USERNAME)
            .passwordParameter(PASSWORD)
            .successHandler(authSuccessHandler)
            .failureHandler(authFailureHandler)*/
        .and()
            .logout().permitAll()
            .addLogoutHandler(lemonSaveSessionIdLogoutHandler)
            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PATH, "DELETE"))
            .logoutSuccessHandler(logoutSuccessHandler);
        http.addFilterBefore(concurrentSessionFilter(), WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(lemonRefreshTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(lemonAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(refreshTokenAuthenticationProvider);
        auth.authenticationProvider(lemonAuthenticationProvider);
    }
    
}
