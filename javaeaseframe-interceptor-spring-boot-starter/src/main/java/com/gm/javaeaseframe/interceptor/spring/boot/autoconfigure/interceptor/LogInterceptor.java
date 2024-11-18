package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import com.gm.javaeaseframe.common.annotation.CustomApiOperation;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.common.util.ReflectionUtil;
import com.gm.javaeaseframe.core.context.interceptor.BaseInterceptor;
import com.gm.javaeaseframe.core.context.service.ILogService;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.LogInterceptorProperties;

/**
 * 日志拦截器
 * @author	GM
 * @date	2023年9月21日
 */
public class LogInterceptor extends BaseInterceptor {
	
	@Autowired(required=false)
    protected ILogService logService;
	@Autowired
	protected LogInterceptorProperties interceptorConfig;
	
	//请求开始时间标识
    private static final String LOGGER_RECEIVE_TIME = "_receive_time";
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("UTF-8");
        if (interceptorConfig != null && interceptorConfig.isEnable() && interceptorConfig.getPrintPosition() != null) {
        	//设置请求开始时间
            request.setAttribute(LOGGER_RECEIVE_TIME,System.currentTimeMillis());
        	if (interceptorConfig.getPrintPosition().toLowerCase().equalsIgnoreCase("before") 
        	 || interceptorConfig.getPrintPosition().toLowerCase().equalsIgnoreCase("beforeAndAfter")) {
        		this.doPringLog(request, response, handler, null, interceptorConfig.isPrintRequestUser(), interceptorConfig.isPrintRequestBody(), false);
        	}
		}
		return super.preHandle(request, response, handler);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (interceptorConfig != null && interceptorConfig.isEnable() && interceptorConfig.getPrintPosition() != null) {
        	if (interceptorConfig.getPrintPosition().toLowerCase().equalsIgnoreCase("after")) {
        		this.doPringLog(request, response, handler, ex, interceptorConfig.isPrintRequestUser(), interceptorConfig.isPrintRequestBody(), interceptorConfig.isPrintResponseBody());
        	} else if (interceptorConfig.getPrintPosition().toLowerCase().equalsIgnoreCase("beforeAndAfter")) {
        		this.doPringLog(request, response, handler, ex, interceptorConfig.isPrintRequestUser(), false, interceptorConfig.isPrintResponseBody());
        	}
		}
		super.afterCompletion(request, response, handler, ex);
	}
	
	private void doPringLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, 
			boolean printRequestUser, boolean printRequestBody, boolean printResponseBody) {
		String requestUrl = request.getServletPath();
		String requestIp = HttpUtil.getRequestIP(request);
		String requestId = super.getRequestId();
		//当前时间
        long currentTime = System.currentTimeMillis();
        //请求开始时间
        long time = currentTime;
        try {
        	//请求开始时间
        	time = Long.valueOf(request.getAttribute(LOGGER_RECEIVE_TIME).toString());
        } catch (Exception e) {}
        long duration = currentTime - time;
		String content = "";
		IUser user = super.getCurrUser(request);
		try {
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				CustomApiOperation customOperation = handlerMethod.getMethodAnnotation(CustomApiOperation.class);
				if (customOperation != null) {
					content = customOperation.value();
				} else {
					java.lang.annotation.Annotation[] anns = handlerMethod.getMethod().getAnnotations();
					if (anns != null) {
						for (java.lang.annotation.Annotation ann : anns) {
							String name = ann.annotationType().getName();
							if (name.equalsIgnoreCase("io.swagger.annotations.ApiOperation")) {
								Object value = ReflectionUtil.invokeMethod(ann, "value", null, null);
								if (value != null) {
									content = value.toString();
								}
								break;
							}
						}
					}
				}
			}
		} catch(Throwable e){}
		if (printRequestBody) {
			String body = super.getRequestParams(request, interceptorConfig.isFetchFromNativeRequestEnable());
			if (StringUtils.isNotBlank(body)) {
				content += " requestParams=" + body;
			}
		}
		if (printResponseBody) {
			String body = super.getResponseBody(response);
			if (StringUtils.isNotBlank(body)) {
				content += " responseParams=" + body;
			}
		}
		if (ex != null) {
			content += " ==> " + ex.getMessage();
		}
		if (logService != null) {
			logService.doHandlerLog(requestId, requestUrl, requestIp, user, content, duration, printRequestUser);
		}
	}
	
}
