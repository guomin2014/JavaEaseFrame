package com.gm.javaeaseframe.core.context.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gm.javaeaseframe.common.code.PlatformCodeEnum;
import com.gm.javaeaseframe.core.constains.GlobalSysInfo;
import com.gm.javaeaseframe.core.context.service.ILogService;
import com.gm.javaeaseframe.core.context.service.IUser;

public class FileLogServiceImpl implements ILogService
{
	private static FileLogServiceImpl instance = new FileLogServiceImpl();
	
	public static ILogService getInstance()
	{
		return instance;
	}
	public static ILogService getInstance(String loggerName)
	{
		instance.logForOper = LoggerFactory.getLogger(loggerName == null ? "com.gm.normal.framework.log" : loggerName);
		return instance;
	}
	
	/** 操作日志对象 */
    protected Logger logForOper = LoggerFactory.getLogger("com.gm.normal.framework.log");
    
    @Override
	public void doHandlerLog(String platformMark, String loginName, String requestUrl, String content, String ip)
	{
		this.doHandlerLog(platformMark, null, null, loginName, requestUrl, content, ip, new Date());
	}
	@Override
	public void doHandlerLog(String platformMark, Long userId, String userName, String loginName, String requestUrl, String content, 
			String ip, Date logDate)
	{
		this.doHandlerLog(platformMark, null, requestUrl, ip, userId, userName, loginName, null, null, null, content, logDate, null, true);
	}
	
	private void doHandlerLog(String platformMark, String requestId, String requestUrl, String requestIp, 
			Long userId, String userName, String loginName, Integer userType, Integer userRole, Long customerId,
			String content, Date logDate, Long duration, boolean showUser) {
		String uv = StringUtils.isNotBlank(loginName) ? loginName : userName;
		String statDuration = duration == null ? "" : duration.toString();
		requestId = StringUtils.isBlank(requestId) ? "" : requestId;
		if (StringUtils.isBlank(platformMark)) {
			PlatformCodeEnum platform = PlatformCodeEnum.getByValue(GlobalSysInfo.platformCode);
			if (platform != null) {
				platformMark = platform.name();
			}
		}
		if (showUser) {
	    	logForOper.info("statLOG statMOD={} statPV=1 statUV={} statUserId={} statCustomerId={} statUserType={} statUserRole={} statIP={} statRequestId={} statURL={} statDuration={} {}",
	    			platformMark, uv, userId, customerId, userType, userRole, requestIp, requestId, requestUrl, statDuration, content);
		} else {
			logForOper.info("statLOG statMOD={} statPV=1 statIP={} statRequestId={} statURL={} statDuration={} {}",
	    			platformMark, requestIp, requestId, requestUrl, statDuration, content);
		}
	}
	@Override
	public void doHandlerLog(String requestId, String requestUrl, String requestIp, IUser user, String content,
			Long duration) {
		this.doHandlerLog(requestId, requestUrl, requestIp, user, content, duration, true);
	}
	@Override
	public void doHandlerLog(String requestId, String requestUrl, String requestIp, IUser user, String content,
			Long duration, boolean showUser) {
		String platformMark = "";
		PlatformCodeEnum platform = PlatformCodeEnum.getByValue(GlobalSysInfo.platformCode);
		if (platform != null) {
			platformMark = platform.name();
		}
		Long userId = null;
		Long customerId = null;
		String userName = null;
		String loginName = null;
		Integer userType = null;
		Integer userRole = null;
		if (user != null) {
			userId = user.getId();
			loginName = user.getLoginName();
			userName = user.getRealName();
			userType = user.getUserType();
			userRole = user.getUserRole();
			customerId = user.getCustomerId();
		}
		this.doHandlerLog(platformMark, requestId, requestUrl, requestIp, userId, userName, loginName, userType, userRole, customerId, content, null, duration, showUser);
	}
}
