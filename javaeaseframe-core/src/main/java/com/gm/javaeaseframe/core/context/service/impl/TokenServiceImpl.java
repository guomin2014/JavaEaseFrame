package com.gm.javaeaseframe.core.context.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.core.context.model.CookieInfo;
import com.gm.javaeaseframe.core.context.service.ICacheService;
import com.gm.javaeaseframe.core.context.service.ITokenService;

@Service("tokenService")
public class TokenServiceImpl implements ITokenService
{

	private final String SUFFIX_COOKIE = "token:user:id:";
	@Autowired(required=false)
	private ICacheService cacheService;
	@Override
	public boolean saveToken(CookieInfo cookie) throws BusinessException
	{
		this.checkCache();
		if(cookie == null || cookie.getUser() == null || StringUtils.isEmpty(cookie.getToken()))
		{
			return false;
		}
		cacheService.sadd(generationKey(cookie), cookie.getToken());
		return false;
	}
	
	@Override
	public void expireToken(CookieInfo cookie) throws BusinessException
	{
		this.checkCache();
		if(cookie == null || cookie.getUser() == null || StringUtils.isEmpty(cookie.getToken()))
		{
			return;
		}
		cacheService.srem(generationKey(cookie), cookie.getToken());
	}
	@Override
	public void expireUser(CookieInfo cookie) throws BusinessException
	{
		this.checkCache();
		if(cookie == null || cookie.getUser() == null)
		{
			return;
		}
		cacheService.del(generationKey(cookie));
	}
	@Override
	public boolean isExpireToken(CookieInfo cookie) throws BusinessException
	{
		this.checkCache();
		if(cookie == null || cookie.getUser() == null || StringUtils.isEmpty(cookie.getToken()))
		{
			return true;
		}
		if(cookie.getExpiresTime() != null && System.currentTimeMillis() >= cookie.getExpiresTime().getTime())
		{
			return true;
		}
		return !cacheService.sismembers(generationKey(cookie), cookie.getToken());
	}
	
	private void checkCache() throws BusinessException
	{
		if(cacheService == null)
		{
			throw new BusinessException("缓存服务组件未初始化，不能进行该业务操作");
		}
	}
	private String generationKey(CookieInfo cookie)
	{
		return SUFFIX_COOKIE + cookie.getUser().getId();
	}

}
