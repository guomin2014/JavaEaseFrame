package com.gm.javaeaseframe.core.context.service;

public interface IUser {

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	Long getId();
	/**
	 * 获取用户归属客户ID
	 * 
	 * @return
	 */
	Long getCustomerId();
	/**
	 * 获取用户归属客户CODE
	 * @return
	 */
	String getCustomerCode();
	/**
	 * 获取用户登录名
	 * @return
	 */
	String getLoginName();
	/**
	 * 获取用户昵称
	 * 
	 * @return
	 */
	String getRealName();
	/**
	 * 获取用户类型 1 系统用户, 2 代理商, 3 终端用户
	 * 
	 * @return
	 */
	Integer getUserType();

	/**
	 * 获取用户岗位 1 管理员, 2 普通用户
	 * 
	 * @return
	 */
	Integer getUserRole();
	/**
	 * 获取用户状态 0 未激活, 1 正常, 2 禁用
	 * 
	 * @return
	 */
	Integer getStatus();
	/**
	 * 是否是超级管理员(userType=1 and userRole=1)
	 * 
	 * @return
	 */
	boolean isAdmin();
	/**
	 * 是否系统用户(userType=1)
	 * @return
	 */
	boolean isSystemUser();
	/**
	 * 是否是管理员(userRole=1)
	 * 
	 * @return
	 */
	boolean isManager();

}
