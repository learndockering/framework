package com.hisun.lemon.common.scanner;

import java.io.IOException;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.hisun.lemon.common.exception.LemonException;

public class ClassScanner {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    
    private Environment environment = new StandardEnvironment();
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    
    public ClassScanner() {
        setResourceLoader(null);
    }
    
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
    
    public void scan(ScannerCallback callback, String... basePackages) {
        doScan(callback, basePackages);
    }
    
    public void doScan(ScannerCallback callback, String... basePackages) {
        for (String basePackage : basePackages) {
            scanAndCallback(callback, basePackage);
        }
    }
    
    public void scanAndCallback(ScannerCallback callback, String basePackage) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + this.resourcePattern;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    String className = annotationMetadata.getClassName();
                    callback.callback(className);
                }
            }
        } catch (IOException e) {
            throw new LemonException("I/O failure during classpath scanning", e);
        }
        
    }
    
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }
}
