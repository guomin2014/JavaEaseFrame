package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.core.common.ExceptionCodeUtil;
import com.gm.javaeaseframe.core.context.interceptor.BaseInterceptor;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties.LoginInterceptorProperties;

/**
 * 登录拦截器
 * 
 * @author GM
 * @date 2023年11月6日
 */
public class LoginInterceptor extends BaseInterceptor {

	@Autowired
	private LoginInterceptorProperties config;

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 10;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (config.isEnable()) {
			try {
				String uri = request.getServletPath();
				if (config.needCheckLogin(uri)) {
					IUser user = super.getCurrUser(request);
					// 判断用户是否登录
					if (user == null) {
						CommonResult<?> result = new CommonResult<>();
						result.setCode(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.AUTH_USER_INVALID.getCode()));
						result.setMsg("用户未登录或登录失效");
						writeJsonResponse(response, HttpServletResponse.SC_OK, JSON.toJSONString(result));
						return false;
					}
				}
			} catch (Exception e) {
				logger.error("登录校验拦截请求处理异常-->" + e.getMessage());
				CommonResult<?> result = new CommonResult<>();
				result.setCode(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.AUTH_USER_INVALID.getCode()));
				result.setMsg("用户登录校验异常");
				writeJsonResponse(response, HttpServletResponse.SC_OK, JSON.toJSONString(result));
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}
	
}