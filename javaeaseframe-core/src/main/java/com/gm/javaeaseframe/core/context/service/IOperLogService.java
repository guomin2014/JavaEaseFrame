package com.gm.javaeaseframe.core.context.service;

/**
 * 系统操作日志服务接口(将框架的操作日志对外提供的主入口)
 * @author	GuoMin
 * @date	2025年2月06日
 */
public interface IOperLogService extends IService
{
	/**
	 * 
	 * @param requestId		请求ID
	 * @param requestUrl	请求URL
	 * @param requestIp		请求IP
	 * @param user			操作用户
	 * @param content		操作内容
	 */
	public void doHandlerLog(String requestId, String requestUrl, String requestIp, IUser user, String content);
}
