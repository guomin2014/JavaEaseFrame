package com.gm.javaeaseframe.core.config.web;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gm.javaeaseframe.core.config.web.converter.CustomJsonMessageConverter;
import com.gm.javaeaseframe.core.config.web.resolver.ArgumentResolverModeEnum;
import com.gm.javaeaseframe.core.config.web.resolver.CustomMethodArgumentResolver;
import com.gm.javaeaseframe.core.context.interceptor.BaseInterceptor;
import com.gm.javaeaseframe.core.listener.event.InterceptorRegistryEvent;

public class BaseWebMvcConfigurer implements WebMvcConfigurer {

    protected Log log = LogFactory.getLog(getClass());
    
    /** 是否使用自定义参数转换器 */
    private boolean useCustomArgumentResolver = false; 
    /** 参数转换模式，默认为全部 */
    private ArgumentResolverModeEnum argumentResolverMode = ArgumentResolverModeEnum.ALL;
    
    @Autowired(required=false)
    private List<BaseInterceptor> interList;
    
    @Autowired(required=false)
    private ApplicationContext applicationContext;
    
    public BaseWebMvcConfigurer(boolean useCustomArgumentResolver, ArgumentResolverModeEnum argumentResolverMode) {
        this.useCustomArgumentResolver = useCustomArgumentResolver;
        if (argumentResolverMode != null) {
            this.argumentResolverMode = argumentResolverMode;
        }
    }
    
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (interList != null && !interList.isEmpty()) {
            java.util.Collections.sort(interList, new Comparator<BaseInterceptor>() {
                @Override
                public int compare(BaseInterceptor o1, BaseInterceptor o2) {
                    return o1.getOrder() > o2.getOrder() ? 1 : -1;
                }
            });
            for (BaseInterceptor interceptor : interList) {
                if (interceptor != null) {
                    registry.addInterceptor(interceptor).addPathPatterns(interceptor.getPathPatterns());
                    log.info("注册拦截器--->" + interceptor.getClass().getName());
                }
            }
        }
        if (applicationContext != null) {
        	applicationContext.publishEvent(new InterceptorRegistryEvent("Interceptor registration completed"));
        }
    }
    /**
     * 添加参数转换器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if (useCustomArgumentResolver) {
            CustomMethodArgumentResolver resolver = this.customMethodArgumentResolver();
            log.info("添加自定义参数转换器-->" + resolver.getClass().getName());
            resolvers.add(resolver);
        }
    }
    @Bean(name = "customMethodArgumentResolver")
    public CustomMethodArgumentResolver customMethodArgumentResolver() {
        return new CustomMethodArgumentResolver(this.argumentResolverMode);
    }
    /**
     * 添加消息转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        CustomJsonMessageConverter customJsonMessageConverter = new CustomJsonMessageConverter();
        converters.add(customJsonMessageConverter);
    }
}
