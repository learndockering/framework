package com.hisun.lemon.framework.core;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.NetUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.utils.IdGenUtils;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.framework.utils.WebUtils;

/**
 * 初始化lemondata
 * @author yuzhou
 * @date 2017年9月8日
 * @time 上午11:21:45
 *
 */
@Configuration
public class ProxyInitLemonDataConfiguration implements ImportAware{
    private AnnotationAttributes enableInitLemonData;
    
    @SuppressWarnings("unchecked")
    @Bean(name = "initLemonDataAdvisor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryInitLemonDataAdvisor initLemonDataAdvisor() {
        BeanFactoryInitLemonDataAdvisor advisor = new BeanFactoryInitLemonDataAdvisor();
        advisor.setAdvice(initLemonDataInterceptor());
        advisor.setOrder(this.enableInitLemonData.<Integer>getNumber("order"));
        Class<? extends Annotation>[] classes = (Class<? extends Annotation>[])this.enableInitLemonData.getClassArray("supportAnnotations");
        advisor.addSupportInitLemonDataAnnoataion(Arrays.asList(classes));
        String[] supportAnnotationsClassNames = this.enableInitLemonData.getStringArray("supportAnnotationClassNames");
        List<Class<? extends Annotation>> classList = Stream.of(supportAnnotationsClassNames).filter(ReflectionUtils::isPresent).map(ReflectionUtils::forName)
            .filter(Class::isAnnotation).map(c -> (Class<? extends Annotation>) c).collect(Collectors.toList());
        advisor.addSupportInitLemonDataAnnoataion(classList);
        return advisor;
    }
    
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableInitLemonData = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableInitLemonData.class.getName(), false));
        if (this.enableInitLemonData == null) {
            throw new IllegalArgumentException(
                    "@EnableInitLemonData is not present on importing class " + importMetadata.getClassName());
        }
    }
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public InitLemonDataInterceptor initLemonDataInterceptor() {
        InitLemonDataInterceptor interceptor = new InitLemonDataInterceptor();
        interceptor.setLemonDataInitializer(backgroundLemonDataInitializer());
        return interceptor;
    }
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LemonDataInitializer backgroundLemonDataInitializer() {
        return new LemonDataInitializer() {
            private static final String DEFAULT_SOURCE = "BAK";
            @Override
            public void initLemonData() {
                LemonData lemonData = LemonHolder.getLemonData();
                lemonData.setRequestId(IdGenUtils.generateRequestId());
                lemonData.setMsgId(IdGenUtils.generateMsgId());
                lemonData.setAccDate(DateTimeUtils.getCurrentLocalDate());
                lemonData.setLocale(WebUtils.resolveLocale(null));
                lemonData.setStartDateTime(DateTimeUtils.getCurrentLocalDateTime());
                lemonData.setRouteInfo(LemonUtils.getApplicationName());
                lemonData.setUserId(null);
                lemonData.setChannel(null);
                lemonData.setClientIp(NetUtils.getLocalHostAddress());
                lemonData.setSource(DEFAULT_SOURCE);
                lemonData.setBusiness(null);
                lemonData.setUri(null);
                lemonData.setToken(null);
            }
        };
    }

}
