package com.hisun.lemon.swagger;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.swagger.SwaggerProperties.GlobleParam;

/**
 * @author yuzhou
 * @date 2017年9月19日
 * @time 下午4:22:05
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration{
    private static final String CURRENT_ENV = LemonConstants.CURRENT_ENV;
    
    @EnableConfigurationProperties(SwaggerProperties.class)
    @Configuration
    @Conditional(SwaggerEnableCondition.class)
    public static class EnableSwagger2Configuration implements EnvironmentAware{
        private Environment environment;
        private SwaggerProperties swaggerProperties;
        public EnableSwagger2Configuration(SwaggerProperties swaggerProperties) {
            this.swaggerProperties = swaggerProperties;
        }
        
        @PostConstruct
        public void init() {
            this.swaggerProperties.getGlobleRequestParams().entrySet().stream().forEach(entry -> {
                String name = entry.getKey();
                GlobleParam gp = entry.getValue();
                if(JudgeUtils.isEmpty(gp.getName())) {
                    gp.setName(name);
                }
            });
        }
        
        @Bean
        public Docket restfulApi() {
            boolean isShow = isShow();
            String scanPackage = swaggerProperties.getScanPackage();
            if(JudgeUtils.isBlank(scanPackage)) {
                scanPackage = "com.hisun.lemon";
            }
            return new Docket(DocumentationType.SWAGGER_2)
            .enable(isShow)
            .apiInfo(apiInfo())
            .globalOperationParameters(operationParameters())
            .forCodeGeneration(false)
            .select()
                .apis(RequestHandlerSelectors.basePackage(isShow ? scanPackage : "xx.xx.xx"))
               // .apis(isShow ? RequestHandlerSelectors.any() : RequestHandlerSelectors.none()) // 对所有api进行监控
                .paths(isShow ? PathSelectors.any() : PathSelectors.none()) //对所有路径进行监控
            .build()
           // .enableUrlTemplating(isShow)
         //   .genericModelSubstitutes(GenericDTO.class)
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, responseMessages())
            .globalResponseMessage(RequestMethod.POST, responseMessages())
            .globalResponseMessage(RequestMethod.PUT, responseMessages())
            .globalResponseMessage(RequestMethod.DELETE, responseMessages())
            .ignoredParameterTypes(ReflectionUtils.forName("com.hisun.lemon.framework.data.NoBody"));
        }
        
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                .title(getTitle().toUpperCase() )
                .description("current environment is " + environment.getProperty(CURRENT_ENV) 
                     + Optional.ofNullable(this.swaggerProperties.getDescription()).map(m -> "; "+m).orElse("")
                        /*+" <div> <em> 返回对象都包含msgCd、msgInfo属性, 交易执行成功返回XXX00000 </em> </div> "*/)
                .termsOfServiceUrl(this.swaggerProperties.getServiceUrl())
                .contact(new Contact(this.swaggerProperties.getContactName(), this.swaggerProperties.getContactUrl(), this.swaggerProperties.getContactEmail()))
                .version(this.swaggerProperties.getVersion())
                .build();
        }
        
        private List<Parameter> operationParameters() {
            Map<String , GlobleParam> globleRequestParams = this.swaggerProperties.getGlobleRequestParams();
            if(JudgeUtils.isNotEmpty(globleRequestParams)) {
                return globleRequestParams.values().stream().map(gp -> buildParameter(gp)).collect(Collectors.toList());
            }
            return new ArrayList<Parameter>();
        }
        
        private List<ResponseMessage> responseMessages() {
            List<ResponseMessage> responses = new ArrayList<>();
            responses.add(new ResponseMessageBuilder().code(200).message("成功").build());
            responses.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("Error")).build());
            responses.add(new ResponseMessageBuilder().code(401).message("权限认证失败").responseModel(new ModelRef("Error")).build());
            responses.add(new ResponseMessageBuilder().code(403).message("请求资源不可用").responseModel(new ModelRef("Error")).build());
            responses.add(new ResponseMessageBuilder().code(404).message("请求资源不存在").responseModel(new ModelRef("Error")).build());
            
            Map<String , ResponseMessage> globleResponses = this.swaggerProperties.getGlobleResponseMessage();
            if(JudgeUtils.isNotEmpty(globleResponses)) {
                responses.addAll(globleResponses.values());
            }
            return responses;
        }
        
        private Parameter buildParameter(GlobleParam gp) {
            ParameterBuilder parameterBuilder = new ParameterBuilder(); 
            return parameterBuilder.name(gp.getName()).description(gp.getDesc()).parameterType(gp.getType())
            .required(gp.isRequired()).modelRef(new ModelRef(gp.getModelRef())).defaultValue(gp.getDefaultValue()).build();
        }
        
        private boolean isShow() {
           return true;
        }
        
        private String getTitle() {
            return "APIs for " + this.environment.getProperty(LemonConstants.APPLICATION_NAME);
        }
        
        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
    
    
    @Configuration
    @Conditional(SwaggerDisableCondition.class)
    public static class DisableSwagger2Configuration implements EnvironmentAware{
        private Environment environment;
        @Bean
        public Docket restfulApi() {
            boolean isShow = false;
            return new Docket(DocumentationType.SWAGGER_2)
            .enable(isShow)
            .apiInfo(apiInfo())
            .forCodeGeneration(false)
            .select()
                .apis(RequestHandlerSelectors.basePackage("xx.xx.xx"))
                .paths(PathSelectors.none())
            .build()
            .useDefaultResponseMessages(false)
            .ignoredParameterTypes(ReflectionUtils.forName("com.hisun.lemon.framework.data.NoBody"));
        }
        
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                .title(getTitle().toUpperCase() )
                .description("current environment is " + environment.getProperty(CURRENT_ENV))
                .build();
        }
        
        private String getTitle() {
            return "No swagger for " + this.environment.getProperty(LemonConstants.APPLICATION_NAME);
        }
        
        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
   
}
