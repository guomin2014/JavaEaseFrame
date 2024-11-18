package com.gm.javaeaseframe.core.context.service;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.model.CookieInfo;

public interface ITokenService extends IService
{
	/**
	 * 存储token信息
	 * @param cookie
	 * @return
	 * @throws BusinessException
	 */
	boolean saveToken(CookieInfo cookie) throws BusinessException;
	/**
	 * 设置token过期
	 * @param cookie
	 * @throws BusinessException
	 */
	void expireToken(CookieInfo cookie) throws BusinessException;
	/**
	 * 设置该用户的所有token过期
	 * @param cookie
	 * @throws BusinessException
	 */
	void expireUser(CookieInfo cookie) throws BusinessException;
	/**
	 * token是否过期
	 * @param cookie
	 * @return	true：过期，false：未过期
	 * @throws BusinessException
	 */
	boolean isExpireToken(CookieInfo cookie) throws BusinessException;
}
