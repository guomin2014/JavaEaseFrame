package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.properties;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".interceptor.auth")
public class AuthInterceptorProperties {

	private boolean enable;
	
	/**
	 * 不需要权限的URL地址，多个用逗号分隔
	 */
	private String uncheckUrl;
	
	private Set<String> uncheckUrls = new HashSet<>();
	/** 有后缀占位符的登录不检验地址 */
	private Set<String> uncheckUrlsSuffix = new HashSet<>();

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public String getUncheckUrl() {
		return uncheckUrl;
	}

	public void setUncheckUrl(String uncheckUrl) {
		this.uncheckUrl = uncheckUrl;
	}

	/**
	 * 校验地址是否需要检查权限状态
	 * @param url
	 * @return true：需要，false：不需要
	 */
	public boolean needCheckAuth(String url) {
	    if (uncheckUrls.contains(url)) {
	        return false;
	    }
	    for (String regexUrl : uncheckUrlsSuffix) {
	        if (url.startsWith(regexUrl)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	@PostConstruct
	public void convert()
	{
		uncheckUrls.clear();
		uncheckUrls.addAll(this.converStr2Set(uncheckUrl));
		uncheckUrlsSuffix.clear();
		for (String url : uncheckUrls) {
		    int index = url.indexOf("*");
		    if (index != -1) {
		        uncheckUrlsSuffix.add(url.substring(0, index));
		    }
		}
	}
	
	public Set<String> converStr2Set(String strs) {
		return converStr2Set(strs, ",");
	}

	public Set<String> converStr2Set(String strs, String split) {
		Set<String> retList = new HashSet<String>();
		if (StringUtils.isEmpty(strs)) {
			return retList;
		}
		if (split == null) {
			split = ",";
		}
		String[] strTmp = strs.split(split);
		for (String ss : strTmp) {
			if (StringUtils.isNotEmpty(ss)) {
				retList.add(ss.trim());
			}
		}
		return retList;
	}

	
}
