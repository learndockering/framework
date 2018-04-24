package com.hisun.lemon.swagger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import springfox.documentation.service.ResponseMessage;

@ConfigurationProperties("swagger")
public class SwaggerProperties {
    private boolean show;
    private String description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    private String serviceUrl;
    private String scanPackage;
    
    private Map<String, GlobleParam> globleRequestParams = new LinkedHashMap<>();
    private Map<String, ResponseMessage> globleResponseMessage = new LinkedHashMap<>();
    
    
    public boolean isShow() {
        return show;
    }
    public void setShow(boolean show) {
        this.show = show;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactUrl() {
        return contactUrl;
    }
    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getServiceUrl() {
        return serviceUrl;
    }
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    public String getScanPackage() {
        return scanPackage;
    }
    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }
    public Map<String, GlobleParam> getGlobleRequestParams() {
        return globleRequestParams;
    }
    public void setGlobleRequestParams(Map<String, GlobleParam> globleRequestParams) {
        this.globleRequestParams = globleRequestParams;
    }

    public Map<String, ResponseMessage> getGlobleResponseMessage() {
        return globleResponseMessage;
    }
    public void setGlobleResponseMessage(
            Map<String, ResponseMessage> globleResponseMessage) {
        this.globleResponseMessage = globleResponseMessage;
    }

    public static class GlobleParam {
        private String name;
        private String desc;
        private String type;
        private String modelRef;
        private String defaultValue;
        private boolean required = false;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getModelRef() {
            return modelRef;
        }
        public void setModelRef(String modelRef) {
            this.modelRef = modelRef;
        }
        public boolean isRequired() {
            return required;
        }
        public void setRequired(boolean required) {
            this.required = required;
        }
        public String getDefaultValue() {
            return defaultValue;
        }
        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }
        
    }
}
