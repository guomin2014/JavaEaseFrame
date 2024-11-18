package com.gm.javaeaseframe.web.spring.boot.autoconfigure.properties;

public class CacheBodyProperties {

	/** 是否启用缓存 */
	private boolean enable = true;
	/** 缓存RequestBody的地址规则，多个使用逗号分隔 */
	private String[] urlPatterns = new String[]{"/*"};
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String[] getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(String[] urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	
}
