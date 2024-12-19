package com.gm.javaeaseframe.xxljob.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".xxljob")
public class XxlJobProperties {

	private String adminAddresses;
    private String accessToken;
    private String appname;
    private String address;
    private String ip;
    private int port;
    private String logPath;
    private int logRetentionDays;
	
	public String getAdminAddresses() {
		return adminAddresses;
	}
	public void setAdminAddresses(String adminAddresses) {
		this.adminAddresses = adminAddresses;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public int getLogRetentionDays() {
		return logRetentionDays;
	}
	public void setLogRetentionDays(int logRetentionDays) {
		this.logRetentionDays = logRetentionDays;
	}
	
}
