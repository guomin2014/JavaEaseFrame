package com.gm.javaeaseframe.core.config.web.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * 自定义参数解析器，支持json格式和表单形式的数据解析
 * @author	GM
 * @date	2020年10月29日
 */
public class CustomMethodArgumentResolver implements HandlerMethodArgumentResolver, Ordered, ApplicationListener<ApplicationReadyEvent> {

    private Log logger = LogFactory.getLog(CustomMethodArgumentResolver.class);
    
    private List<HandlerMethodArgumentResolver> localResolver = new ArrayList<>();

    private boolean initResolver;

    /** 对应json格式的数据解析，SpringMVC中已实现 */
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    /** 对应表单格式的数据解析，SpringMVC中已实现 */
    private ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor;
    /** 参数转换模式 */
    private ArgumentResolverModeEnum argumentResolverMode = ArgumentResolverModeEnum.ALL;
    /** 从RequestBody里获取参数的Content-Type */
    private Set<String> contentTypeByRequestBody = new HashSet<>();
    /** 从Form表单里获取参数的Content-Type */
    private Set<String> contentTypeByServletModel = new HashSet<>();
    
    public CustomMethodArgumentResolver() {
        this(ArgumentResolverModeEnum.ALL);
    }
    /**
     * 
     * @param argumentResolverMode  参数转换模式
     */
    public CustomMethodArgumentResolver(ArgumentResolverModeEnum argumentResolverMode) {
        this(argumentResolverMode, null, null);
    }
    /**
     * 
     * @param argumentResolverMode      参数转换模式
     * @param contentTypeByRequestBody  从RequestBody里获取参数的Content-Type
     * @param contentTypeByServletModel 从Form表单里获取参数的Content-Type
     */
    public CustomMethodArgumentResolver(ArgumentResolverModeEnum argumentResolverMode, Set<String> contentTypeByRequestBody, Set<String> contentTypeByServletModel) {
        if (argumentResolverMode != null) {
            this.argumentResolverMode = argumentResolverMode;
        }
        if (contentTypeByRequestBody != null) {
            for (String str : contentTypeByRequestBody) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                this.contentTypeByRequestBody.add(StringUtils.trim(str).toLowerCase());
            }
        } else {//设置默认规则
            this.contentTypeByRequestBody.add("application/json");
            this.contentTypeByRequestBody.add("application/json+form");
        }
        if (contentTypeByServletModel != null) {
            for (String str : contentTypeByServletModel) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                this.contentTypeByServletModel.add(StringUtils.trim(str).toLowerCase());
            }
        } else {
            this.contentTypeByServletModel.add("application/x-www-form-urlencoded");//Form表单
        }
    }
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!initResolver) {
            //响应ApplicationReadyEvent事件，表明tomcat(jetty)容器已将上下文填充完毕，从而从容器中获取json和表单的参数解析器
            if (event instanceof ApplicationReadyEvent) {
                logger.info("开始加载自定义参数处理器...");
                RequestMappingHandlerAdapter requestMappingHandlerAdapter = (RequestMappingHandlerAdapter) ((ApplicationReadyEvent) event).getApplicationContext().getBean("requestMappingHandlerAdapter");
                if (requestMappingHandlerAdapter == null) {
                    throw new RuntimeException("自定义参数解析器加载失败");
                }
                for (HandlerMethodArgumentResolver resolver : requestMappingHandlerAdapter.getArgumentResolvers()) {
                    if (resolver instanceof RequestResponseBodyMethodProcessor) { //获取json形式的解析器
                        this.requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) resolver;
                    } else if (resolver instanceof ServletModelAttributeMethodProcessor) { //获取表单形式的解析器
                        this.servletModelAttributeMethodProcessor = (ServletModelAttributeMethodProcessor) resolver;
                    }
                }
                localResolver.addAll(requestMappingHandlerAdapter.getArgumentResolvers());
                this.initResolver = true;
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean methodNotSupport = parameter.hasMethodAnnotation(FormAndJsonArgumentResolverNotSupport.class);
        boolean classNotSupport = parameter.getContainingClass().isAnnotationPresent(FormAndJsonArgumentResolverNotSupport.class);
        if (methodNotSupport || classNotSupport) {//方法或类上标识不支持
            return false;
        }
        if (argumentResolverMode == null) {
            return false;
        }
        switch (argumentResolverMode) {
            case ALL:
                return true;
            case BLACK://黑名单方式，只要不明确指定不支持，能可以进行转换
                return true;
            case WHITE://白名单方式，包含指标标签的方式才能进行转换
                //判断方法上是否存在
                boolean methodSupport = parameter.hasMethodAnnotation(FormAndJsonArgumentResolverSupport.class);
                if (methodSupport) {
                    return true;
                } else {
                    //判断类上是否存在
                    return parameter.getContainingClass().isAnnotationPresent(FormAndJsonArgumentResolverSupport.class);
                }
            default:
                return false;
        }
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String contentType = StringUtils.trim(webRequest.getHeader("Content-Type")).toLowerCase();
        //根据请求头Content-Type，判断是什么形式提交
        //json形式
        if (this.supportsRequestBody(contentType) && requestResponseBodyMethodProcessor != null) {
            return requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        } else if (this.supportsServletModel(contentType) && servletModelAttributeMethodProcessor != null){//默认都使用表单形式
            return servletModelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        } else {
            return doContinueResolver(localResolver, parameter, mavContainer, webRequest, binderFactory);
        }
    }
    /**
     * 使用其它解析器继续解析
     * @param localResolver
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    private Object doContinueResolver(List<HandlerMethodArgumentResolver> localResolver, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (localResolver == null || localResolver.isEmpty()) {
            throw new RuntimeException("未找到匹配的解析器[Content-Type:" + webRequest.getHeader("Content-Type") + "]，无法解析该参数:" + parameter.getParameter());
        }
        for (HandlerMethodArgumentResolver resolver : localResolver) {
            if (resolver instanceof CustomMethodArgumentResolver) {
                continue;
            }
            if (resolver.supportsParameter(parameter)) {
                return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
            }
        }
        throw new RuntimeException("未找到匹配的解析器[Content-Type:" + webRequest.getHeader("Content-Type") + "]，无法解析该参数:" + parameter.getParameter());

    }
    
    private boolean supportsRequestBody(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        if (this.contentTypeByRequestBody == null || this.contentTypeByRequestBody.isEmpty()) {
            return false;
        }
        for (String contentTypeRule : contentTypeByRequestBody) {
            if (contentType.startsWith(contentTypeRule)) {
                return true;
            }
        }
        return false;
    }
    private boolean supportsServletModel(String contentType) {
        if (StringUtils.isEmpty(contentType)) {//默认使用Form表单
            return true;
        }
        if (this.contentTypeByServletModel == null || this.contentTypeByServletModel.isEmpty()) {
            return false;
        }
        for (String contentTypeRule : contentTypeByServletModel) {
            if (contentType.startsWith(contentTypeRule)) {
                return true;
            }
        }
        return false;
    }
}
