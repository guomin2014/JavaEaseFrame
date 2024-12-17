package com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".web")
public class WebProperties {

	/** 缓存RequestBody的地址规则 */
	private CacheBodyProperties cacheBody;
	/** 静态资源映射集合 */
	private Map<String, String> resourceHandlers;
	/** HTTP消息转换器配置 */
    private MessageConverterProperties messageConverter;

	public CacheBodyProperties getCacheBody() {
		return cacheBody;
	}

	public void setCacheBody(CacheBodyProperties cacheBody) {
		this.cacheBody = cacheBody;
	}

	public Map<String, String> getResourceHandlers() {
		return resourceHandlers;
	}

	public void setResourceHandlers(Map<String, String> resourceHandlers) {
		this.resourceHandlers = resourceHandlers;
	}

	public MessageConverterProperties getMessageConverter() {
		return messageConverter;
	}

	public void setMessageConverter(MessageConverterProperties messageConverter) {
		this.messageConverter = messageConverter;
	}
	
}
