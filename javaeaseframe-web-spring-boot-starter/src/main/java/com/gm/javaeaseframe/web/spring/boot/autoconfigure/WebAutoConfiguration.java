package com.gm.javaeaseframe.web.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gm.javaeaseframe.common.code.PlatformConstants;
import com.gm.javaeaseframe.core.config.web.filter.CachingRequestBodyFilter;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.advice.GlobalExceptionAdvice;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.advice.GlobalResponseAdvice;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties.CacheBodyProperties;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties.WebMvcConfig;
import com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties.WebProperties;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebProperties.class})
public class WebAutoConfiguration {

	@Autowired
	private WebProperties webProperties;
	
	@Bean
	public WebMvcConfigurer customMvcConfig() {
		return new WebMvcConfig(webProperties);
	}
	
	/**
     * 注册RequestBody缓存过滤器（可以多次重复读取RequestBody）
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = PlatformConstants.PLATFORM_CONFIG_PREFIX + ".web.cacheBody.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<?> cachingRequestBodyFilter() {
    	String[] urlPatterns = null;
    	CacheBodyProperties cacheBody = webProperties.getCacheBody();
    	if (cacheBody != null) {
    		urlPatterns = cacheBody.getUrlPatterns();
    	}
    	if (urlPatterns == null || urlPatterns.length == 0) {
    		urlPatterns = new String[] {"/*"};
    	}
        FilterRegistrationBean<CachingRequestBodyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        CachingRequestBodyFilter filter = new CachingRequestBodyFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns(urlPatterns);//配置过滤规则，只对APP请求有效
        filterRegistrationBean.setName("cachingRequestBodyFilter");//设置过滤器名称
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);//执行次序
        return filterRegistrationBean;
    }
    /**
     * 解决前端访问Fiegn时候出现 Access-Control-Allow-Origin
     * @return
     *
     */
    @Bean
    @ConditionalOnProperty(name = PlatformConstants.PLATFORM_CONFIG_PREFIX + ".web.cors.enable", havingValue = "true", matchIfMissing = false)
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
//        config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。
        config.addAllowedOriginPattern("*");//新版本使用该属性,允许向该服务器提交请求的URI，*表示全部允许。
        config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    @Bean
    public GlobalExceptionAdvice globalExceptionAdvice() {
    	return new GlobalExceptionAdvice();
    }
    @Bean
    public GlobalResponseAdvice globalResponseAdvice() {
    	return new GlobalResponseAdvice();
    }
}
