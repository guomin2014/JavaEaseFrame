package com.gm.javaeaseframe.openfeign.spring.boot.autoconfigure.interceptor;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gm.javaeaseframe.common.util.SnowflakeGenerator;
import com.gm.javaeaseframe.core.constains.SysConstains;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign拦截器，用于将请求的header信息传递
 * 
 * @author GM
 * @date 2023年9月19日
 */
public class FeignRequestInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate template) {
		try {
			//生成请求ID
			template.header(SysConstains.REQUEST_ID, this.createRequestId());
		} catch (Exception e) {}
		// 从RequestContextHolder中获取HttpServletRequest
		RequestAttributes request = RequestContextHolder.getRequestAttributes();
		if (request == null || !(request instanceof ServletRequestAttributes)) {
			return;
		}
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes)request).getRequest();
		// 获取RequestContextHolder中的信息
		Map<String, String> headers = getHeaders(httpServletRequest);
		// 放入feign的RequestTemplate中
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			if (HttpHeaders.ACCEPT.equalsIgnoreCase(name)) {
				continue;
			}
			// 跳过content-length，不然可能会报too many bites written问题
			if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
				continue;
			}
			if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
				continue;
			}
			template.header(name, value);
		}
//		// 配置客户端IP
//		template.header("X-Forwarded-For", IpUtils.getIpAddr());
	}

	/**
	  *  获取原请求头
	 * @param request
	 * @return
	 */
	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enumeration = request.getHeaderNames();
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				String value = request.getHeader(key);
				map.put(key, value);
			}
		}
		return map;
	}
	/**
	 * 生成请求ID
	 * @return
	 */
	private String createRequestId() {
		try {
			//生成请求ID
			return SnowflakeGenerator.nextId().toString();
		} catch (Exception e) {
			return UUID.randomUUID().toString();
		}
	}
}
