package com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties;

import java.util.List;

public class MessageConverterHandler {

	/** 是否启用fastjson转换 */
	private boolean enable = true;
	/** 是否启用排序 */
	private boolean sortEnable = false;
	/** 支持的mediaType */
	private List<String> supportedMediaTypes;
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isSortEnable() {
		return sortEnable;
	}
	public void setSortEnable(boolean sortEnable) {
		this.sortEnable = sortEnable;
	}
	public List<String> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}
	public void setSupportedMediaTypes(List<String> supportedMediaTypes) {
		this.supportedMediaTypes = supportedMediaTypes;
	}
	
	
}
