package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor.AuthInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor.LogInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor.LoginInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.AuthInterceptorProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.LogInterceptorProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.LoginInterceptorProperties;

@Configuration
@EnableConfigurationProperties({LogInterceptorProperties.class, LoginInterceptorProperties.class, AuthInterceptorProperties.class})
public class InterceptorAutoConfiguration {

	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}
	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}
	@Bean
	public LogInterceptor logInterceptor() {
		return new LogInterceptor();
	}
}
