package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.auth.interceptor.AuthInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.auth.properties.AuthInterceptorProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.interceptor.LogInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties.LogInterceptorProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.login.interceptor.LoginInterceptor;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.login.properties.LoginInterceptorProperties;

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
