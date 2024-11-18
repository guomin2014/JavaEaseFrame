package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.core.common.ExceptionCodeUtil;
import com.gm.javaeaseframe.core.context.interceptor.BaseInterceptor;
import com.gm.javaeaseframe.core.context.service.IAuthService;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.AuthInterceptorProperties;

/**
 * 权限拦截器(需要晚于登录拦截器)
 * 
 * @author GM
 * @date 2023年11月6日
 */
public class AuthInterceptor extends BaseInterceptor {

	@Autowired(required = false)
	private IAuthService authService;
	@Autowired
	private AuthInterceptorProperties config;

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 9;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (config.isEnable()) {
			try {
				String uri = request.getServletPath();
				if (config.needCheckAuth(uri)) {
					String requestUrl = request.getServletPath();
					String requestIp = HttpUtil.getRequestIP(request);
					String requestId = super.getRequestId();
					IUser user = super.getCurrUser(request);
					// 判断用户是否有权限
					if (authService == null || !authService.checkAuth(requestId, requestUrl, requestIp, user)) {
						CommonResult<?> result = new CommonResult<>();
						result.setCode(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.CLIENT_REQUEST_FORBIDDEN.getCode()));
						result.setMsg("您没有该操作权限");
						writeJsonResponse(response, HttpServletResponse.SC_OK, JSON.toJSONString(result));
						return false;
					}
				}
			} catch (Exception e) {
				logger.error("权限校验拦截请求处理异常-->" + e.getMessage());
				CommonResult<?> result = new CommonResult<>();
				result.setCode(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.CLIENT_REQUEST_FORBIDDEN.getCode()));
				result.setMsg("您没有该操作权限");
				writeJsonResponse(response, HttpServletResponse.SC_OK, JSON.toJSONString(result));
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}
	
}