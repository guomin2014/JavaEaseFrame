package com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties;

public class MessageConverterProperties {

	/** FastJson配置 */
	private MessageConverterHandler fastJson = new MessageConverterHandler();

	public MessageConverterHandler getFastJson() {
		return fastJson;
	}

	public void setFastJson(MessageConverterHandler fastJson) {
		this.fastJson = fastJson;
	}
	
}
