package com.gm.javaeaseframe.core.context.model;

import java.io.Serializable;
import java.util.Date;

import com.gm.javaeaseframe.core.context.service.IUser;

public class CookieInfo implements Serializable
{
	private static final long serialVersionUID = -3690695786556086411L;
	/** token值，cookie的唯一识别串 */
	private String token;
	/** cookie创建时间 */
	private Date createTime;
	/** cookie过期时间 */
	private Date expiresTime;
	/** 登录用户 */
	private IUser user;
	
	public CookieInfo(){}
	
	public CookieInfo(String token){
		this.token = token;
	}
	public CookieInfo(String token, Date createTime, Date expiresTime, IUser user){
		this.token = token;
		this.createTime = createTime;
		this.expiresTime = expiresTime;
		this.user = user;
	}
	
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Date getExpiresTime()
	{
		return expiresTime;
	}
	public void setExpiresTime(Date expiresTime)
	{
		this.expiresTime = expiresTime;
	}
	public IUser getUser()
	{
		return user;
	}
	public void setUser(IUser user)
	{
		this.user = user;
	}
	
}
