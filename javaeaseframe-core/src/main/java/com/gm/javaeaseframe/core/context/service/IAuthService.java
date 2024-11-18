package com.gm.javaeaseframe.core.context.service;

/**
 * 权限服务接口
 * 
 * @author GM
 * @date 2023年11月6日
 */
public interface IAuthService {

	/**
	 * 检查请求是否有权限
	 * @param requestId
	 * @param requestUrl
	 * @param requestIp
	 * @param user
	 * @return	true:有权限
	 */
	boolean checkAuth(String requestId, String requestUrl, String requestIp, IUser user);
}
