package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import com.gm.javaeaseframe.common.annotation.CustomApiOperation;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.common.util.ReflectionUtil;
import com.gm.javaeaseframe.core.constains.GlobalSysInfo;
import com.gm.javaeaseframe.core.context.interceptor.BaseInterceptor;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties.LogInterceptorProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties.LogPrintProperties;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties.LogPrintProperties.VariableName;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.service.ILogService;

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
    private static final String LOGGER_RECEIVE_TIME = "__receive_time";
    
    private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("UTF-8");
        //设置请求开始时间
        request.setAttribute(LOGGER_RECEIVE_TIME,System.currentTimeMillis());
        this.doPringLog(request, response, handler, null, interceptorConfig.getPrintBeforeAdvice());
		return super.preHandle(request, response, handler);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		this.doPringLog(request, response, handler, ex, interceptorConfig.getPrintAfterAdvice());
		super.afterCompletion(request, response, handler, ex);
	}
	
	private void doPringLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, LogPrintProperties logPrintProperties) {
		if (logPrintProperties == null || !logPrintProperties.isEnable()) {
			return;
		}
		IUser user = super.getCurrUser(request);
		boolean fetchFromNativeRequestEnable = logPrintProperties.isFetchFromNativeRequestEnable();
		String format = logPrintProperties.getFormat();
		Set<String> variableNames = logPrintProperties.getVariableNames();
		Map<String, String> variableValueMap = new HashMap<>();
		if (variableNames != null) {
			for (String variableName : variableNames) {
				VariableName vn = VariableName.getByName(variableName);
				if (vn != null) {
					String variableValue = null;
					try {
						switch (vn) {
							case MOD:
								variableValue = GlobalSysInfo.getPropertyValue("com.gm.javaeaseframe.module");
								break;
							case PV:
								variableValue = "1";
								break;
							case UV:
								variableValue = user != null ? user.getLoginName() : "";
								break;
							case URL:
								variableValue = request.getServletPath();
								break;
							case IP:
								variableValue = HttpUtil.getRequestIP(request);
								break;
							case RequestId:
								variableValue = super.getRequestId();
								break;
							case Duration:
								//当前时间
						        long currentTime = System.currentTimeMillis();
						        //请求开始时间
						        long time = currentTime;
						        try {
						        	//请求开始时间
						        	time = Long.valueOf(request.getAttribute(LOGGER_RECEIVE_TIME).toString());
						        } catch (Exception e) {}
						        long duration = currentTime - time;
						        variableValue = String.valueOf(duration);
								break;
							case UserId:
								variableValue = user != null && user.getId() != null ? user.getId().toString() : "";
								break;
							case UserName:
								variableValue = user != null ? user.getRealName() : "";
								break;
							case UserType:
								variableValue = user != null && user.getUserType() != null ? user.getUserType().toString() : "";
								break;
							case LoginName:
								variableValue = user != null ? user.getLoginName() : "";
								break;
							case Message:
								try {
									if (handler instanceof HandlerMethod) {
										HandlerMethod handlerMethod = (HandlerMethod) handler;
										CustomApiOperation customOperation = handlerMethod.getMethodAnnotation(CustomApiOperation.class);
										if (customOperation != null) {
											variableValue = customOperation.value();
										} else {
											java.lang.annotation.Annotation[] anns = handlerMethod.getMethod().getAnnotations();
											if (anns != null) {
												for (java.lang.annotation.Annotation ann : anns) {
													String name = ann.annotationType().getName();
													if (name.equalsIgnoreCase("io.swagger.annotations.ApiOperation")) {
														Object value = ReflectionUtil.invokeMethod(ann, "value", null, null);
														if (value != null) {
															variableValue = value.toString();
														}
														break;
													}
												}
											}
										}
									}
								} catch(Throwable e){}
								break;
							case RequestParams:
								variableValue = super.getRequestParams(request, fetchFromNativeRequestEnable);
								break;
							case ResponseParams:
								variableValue = super.getResponseBody(response);
								if (StringUtils.isBlank(variableValue) && ex != null) {
									variableValue = ex.getMessage();
								}
								break;
						}
					} catch (Exception e) {}
					if (variableValue == null) {
						variableValue = "";
					}
					variableValueMap.put(vn.getKey(), variableValue);
				} else {
					variableValueMap.put(VariableName.getKeyByName(variableName), "");
				}
			}
		}
		if (logService != null) {
			logService.doHandlerLog(format, variableValueMap);
		} else {
			String log = format;
			if (variableValueMap != null) {
				for (Map.Entry<String, String> entry : variableValueMap.entrySet()) {
					log = log.replace(entry.getKey(), entry.getValue());
				}
			}
			logger.info(log);
		}
	}
}
