package com.gm.javaeaseframe.core.context.service.impl;

import com.gm.javaeaseframe.core.context.service.IUser;

public class User implements IUser {

	/** 用户ID */
	private Long id;
	/** 用户归属客户ID */
	private Long customerId;
	/** 用户归属客户CODE */
	private String customerCode;
	/** 用户登录名 */
	private String loginName;
	/** 用户昵称 */
	private String realName;
	/** 用户类型 1 系统用户, 2 代理商, 3 终端用户 */
	private Integer userType;
	/** 用户岗位 1 管理员, 2 普通用户 */
	private Integer userRole;
	/** 用户状态 0 未激活, 1 正常, 2 禁用 */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean isAdmin() {
		return this.userType != null && this.userRole != null && this.userType == 1 && this.userRole == 1;
	}

	@Override
	public boolean isSystemUser() {
		return this.userType != null && this.userType == 1;
	}

	@Override
	public boolean isManager() {
		return this.userRole != null && this.userRole == 1;
	}

}
