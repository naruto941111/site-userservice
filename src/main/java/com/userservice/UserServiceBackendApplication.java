package com.userservice;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceBackendApplication {
	private static final Logger log = LoggerFactory.getLogger(UserServiceBackendApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(UserServiceBackendApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("admin123"));
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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
			log.error("messagesource loader failed {}", ex.getMessage());
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
