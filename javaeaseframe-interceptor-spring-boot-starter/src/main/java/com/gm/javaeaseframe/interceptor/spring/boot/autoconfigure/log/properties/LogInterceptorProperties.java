package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.properties;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".interceptor.log")
public class LogInterceptorProperties {

	private Map<String, LogPrintProperties> printPosition;
	
	@PostConstruct
    public void init() {
        if (printPosition != null) {
        	for(LogPrintProperties pp : printPosition.values()) {
        		pp.init();
        	}
        }
    }

	public Map<String, LogPrintProperties> getPrintPosition() {
		return printPosition;
	}

	public void setPrintPosition(Map<String, LogPrintProperties> printPosition) {
		this.printPosition = printPosition;
	}
	
	public LogPrintProperties getPrintBeforeAdvice() {
		return printPosition != null ? printPosition.get("beforeAdvice") : null;
	}
	public LogPrintProperties getPrintAfterAdvice() {
		return printPosition != null ? printPosition.get("afterAdvice") : null;
	}
	
}
