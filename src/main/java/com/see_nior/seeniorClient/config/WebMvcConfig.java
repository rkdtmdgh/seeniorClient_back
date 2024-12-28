package com.see_nior.seeniorClient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new AuthenticInterceptor())
			.addPathPatterns("/**");
		
	}
	
	@Bean RestTemplate restTemplate() {
		
		return new RestTemplate();
		
	}

}
