package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@SpringBootApplication
public class OrderServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        ClassLoader classLoader = this.getClass().getClassLoader();
        log.info("message source loading....");
        try{
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
            Resource[] resources = resolver.getResources("lang/messages");
        }
        catch (Exception ex){
            log.error("message source loader failed {}", ex.getMessage());
        }
        messageSource.setBasename("classpath:lang/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.clearCache();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.clearCacheIncludingAncestors();
        return messageSource;
    }
}