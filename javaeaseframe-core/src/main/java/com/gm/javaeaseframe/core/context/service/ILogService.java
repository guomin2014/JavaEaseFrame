package com.gm.javaeaseframe.core.context.service;

import java.util.Date;

/**
 * 日志服务接口(将框架的操作日志对外提供的主入口)
 * @author	GuoMin
 * @date	2016年5月20日
 */
public interface ILogService extends IService
{
	/**
	 * 处理操作日志
	 * @param platformMark	平台标识
	 * @param userId		用户ID
	 * @param userName		用户名称
	 * @param loginName		用户登录名
	 * @param requestUrl	请求地址
	 * @param content		操作内容
	 * @param ip			操作IP地址
	 * @param logDate		操作时间
	 */
	@Deprecated
	public void doHandlerLog(String platformMark, Long userId, String userName, String loginName, String requestUrl, 
			String content, String ip , Date logDate);
	@Deprecated	
	public void doHandlerLog(String platformMark, String loginName, String requestUrl, String content, String ip);
	/**
	 * 
	 * @param requestId		请求ID
	 * @param requestUrl	请求URL
	 * @param requestIp		请求IP
	 * @param user			操作用户
	 * @param content		操作内容
	 * @param duration		持续时长，单位：毫秒
	 */
	public void doHandlerLog(String requestId, String requestUrl, String requestIp, IUser user, String content, Long duration);
	/**
	 * 
	 * @param requestId		请求ID
	 * @param requestUrl	请求URL
	 * @param requestIp		请求IP
	 * @param user			操作用户
	 * @param content		操作内容
	 * @param duration		持续时长，单位：毫秒
	 * @param showUser		显示用户信息
	 */
	public void doHandlerLog(String requestId, String requestUrl, String requestIp, IUser user, String content, Long duration, boolean showUser);
}
