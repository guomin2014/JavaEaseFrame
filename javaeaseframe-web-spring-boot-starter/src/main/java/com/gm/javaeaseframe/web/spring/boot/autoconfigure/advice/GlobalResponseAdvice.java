package com.gm.javaeaseframe.web.spring.boot.autoconfigure.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.gm.javaeaseframe.core.common.ExceptionCodeUtil;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;

/**
 * 统一响应码规则处理（后期国际化可以在该处处理）
 * 
 * @author GM
 * @date 2023年10月24日
 */
@ControllerAdvice()
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		//这里直接返回true,表示对任何handler的responsebody都调用beforeBodyWrite方法
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		if (body != null && body instanceof CommonResult) {//返回错误的编码进行处理
			CommonResult<?> cr = (CommonResult<?>)body;
			if (!cr.isSuccess()) {
				cr.setCode(ExceptionCodeUtil.encodeCode(cr.getCode()));
			}
		}
		return body;
	}

}
