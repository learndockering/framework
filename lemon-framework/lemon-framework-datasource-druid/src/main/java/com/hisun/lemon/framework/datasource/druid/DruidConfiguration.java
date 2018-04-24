package com.hisun.lemon.framework.datasource.druid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * druid configuration
 * 
 * @author yuzhou
 * @date 2017年6月7日
 * @time 上午10:17:01
 *
 */
@Configuration
@ConditionalOnClass({StatViewServlet.class, WebStatFilter.class})
public class DruidConfiguration {
    
    @Value("${lemon.druid.monitor.loginUsername:lemon}")  
    private String loginUsername;
    
    @Value("${lemon.druid.monitor.loginPassword:lemon123}")  
    private String loginPassword;
    
    /**
     * 注册druidServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", this.loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword", this.loginPassword);
        return servletRegistrationBean;
    }
    
    @Bean
    public FilterRegistrationBean duridFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>();
        //设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        return filterRegistrationBean;
    }
    
/*    @Primary
    @ConfigurationProperties(prefix="spring.datasource.druid")
    @Bean
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource(); 
        datasource.setUrl(this.url); 
        datasource.setUsername(this.username); 
        datasource.setPassword(this.password); 
        datasource.setDriverClassName(this.driverClassName); 
        return datasource; 
    }*/
    
   /* @ConfigurationProperties(prefix="lemon.datasource.druid")
    @Bean
    public DataSource lemonDataSource() {
        return new DruidDataSource(); 
    }
    
    @Primary
    @ConfigurationProperties(prefix="primary.datasource.druid")
    @Bean
    public DataSource primaryDataSource() {
        return new DruidDataSource(); 
    }*/

}
