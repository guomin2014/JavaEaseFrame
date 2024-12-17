package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".interceptor.log")
public class LogInterceptorProperties {

	private boolean enable = true;
	/** 是否从原生请求中获取请求Body */
	private boolean fetchFromNativeRequestEnable = false;
	/** 打印请求用户信息 */
	private boolean printRequestUser = true;
	/** 打印请求参数信息 */
	private boolean printRequestBody = false;
	/** 打印响应参数信息 */
	private boolean printResponseBody = false;
	/** 打印位置，前置：before, 后置：after */
	private String printPosition = "after";

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isPrintRequestBody() {
		return printRequestBody;
	}

	public void setPrintRequestBody(boolean printRequestBody) {
		this.printRequestBody = printRequestBody;
	}

	public boolean isPrintResponseBody() {
		return printResponseBody;
	}

	public void setPrintResponseBody(boolean printResponseBody) {
		this.printResponseBody = printResponseBody;
	}

	public boolean isPrintRequestUser() {
		return printRequestUser;
	}

	public void setPrintRequestUser(boolean printRequestUser) {
		this.printRequestUser = printRequestUser;
	}

	public String getPrintPosition() {
		return printPosition;
	}

	public void setPrintPosition(String printPosition) {
		this.printPosition = printPosition;
	}

	public boolean isFetchFromNativeRequestEnable() {
		return fetchFromNativeRequestEnable;
	}

	public void setFetchFromNativeRequestEnable(boolean fetchFromNativeRequestEnable) {
		this.fetchFromNativeRequestEnable = fetchFromNativeRequestEnable;
	}
	
}
