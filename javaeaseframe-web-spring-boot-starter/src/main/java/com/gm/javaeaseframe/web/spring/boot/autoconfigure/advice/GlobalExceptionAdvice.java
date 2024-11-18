package com.gm.javaeaseframe.web.spring.boot.autoconfigure.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.core.common.ExceptionCodeUtil;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;

/**
 * 统一异常处理
 * 
 * @author GM
 * @date 2023年9月19日
 */
@RestControllerAdvice()//annotations = GlobalException.class, 
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionAdvice {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 抛出的异常可能是自定义异常，也可能是其他运行时异常
	@ResponseBody
	@ExceptionHandler(value = { Throwable.class })
	public CommonResult<String> handleFeignStatusException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		//Feign接口的code规则：5位：前2位表示系统编码，后3位表示错误码
		logger.error(e.getMessage(), e);
		// 必须要设置response的status。统一约定服务间调用异常为555错误码
//		response.setStatus(FeignConstains.HTTP_ERROR_STATUS_CODE);
		response.setStatus(200);
		// 构建返回实体
		CommonResult<String> ei = new CommonResult<>();
		// 如果是自定义业务异常
		if (e instanceof BusinessException) {
			BusinessException bize = (BusinessException) e;
			// 自定义的错误码
			ei.setCode(ExceptionCodeUtil.encodeCode(bize.getCode()));
			// 自定义的错误消息提示
			ei.setMsg(bize.getMessage());
			// 请求的URI
			// ei.setPath(request.getRequestURI());
			return ei;
		} else if (e instanceof IllegalArgumentException) {
			// 自定义的错误码
			ei.setCode(ExceptionCodeUtil.encodeCode(ExceptionCodeEnum.PARAM_IS_INVALID.getCode()));
			// 自定义的错误消息提示
			ei.setMsg(ExceptionCodeUtil.encodeMsg(e, ExceptionCodeEnum.PARAM_IS_INVALID.getDesc()));
		} else {
			ExceptionCodeEnum exceptionCode = ExceptionCodeEnum.SYSTEM_ERROR;
//			if (e instanceof FeignException.BadRequest) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_BAD;
//			} else if (e instanceof FeignException.Unauthorized) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_UNAUTHORIZED;
//			} else if (e instanceof FeignException.Forbidden) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_FORBIDDEN;
//			} else if (e instanceof FeignException.NotFound) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_URI_NOT_EXISTS;
//			} else if (e instanceof FeignException.MethodNotAllowed) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_METHOD_NOT_ALLOWED;
//			} else if (e instanceof FeignException.NotAcceptable) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_METHOD_NOT_ACCEPTABLE;
//			} else if (e instanceof FeignException.UnsupportedMediaType) {
//				exceptionCode = ExceptionCodeEnum.CLIENT_REQUEST_MEDIA_TYPE_UNSUPPORTED;
//			} else if (e instanceof FeignException.TooManyRequests) {
//				exceptionCode = ExceptionCodeEnum.SYSTEM_BUSY;
//			} else if (e instanceof FeignException.BadGateway) {
//				exceptionCode = ExceptionCodeEnum.SYSTEM_BAD_GATEWAY;
//			} else if (e instanceof FeignException.ServiceUnavailable) {
//				exceptionCode = ExceptionCodeEnum.SYSTEM_SERVICE_UNAVAILABLE;
//			} else if (e instanceof FeignException.GatewayTimeout) {
//				exceptionCode = ExceptionCodeEnum.SYSTEM_GATEWAY_TIMEOUT;
//			}
			// 自定义的错误码
			ei.setCode(ExceptionCodeUtil.encodeCode(exceptionCode.getCode()));
			// 自定义的错误消息提示
			ei.setMsg(exceptionCode.getDesc());
		}
		return ei;
	}
}
