package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

public class TransactionMethod {
	/** 方法名 */
	private String name;
	/** 传播策略 */
	private String propagation = "REQUIRED";
	/** 是否只读 */
	private boolean readOnly = false;
	/** 事务超时时间，单位：秒，默认-1,永不超时 */
	private int timeout = -1;
	/** 事务隔离级别 */
	private String isolation = "DEFAULT";
	/** 回滚规则 */
	private String rollbackFor = "com.gm.javaeaseframe.common.exception.BusinessException";
	/** 不回滚规则 */
	private String noRollbackFor;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPropagation() {
		return propagation;
	}
	public void setPropagation(String propagation) {
		this.propagation = propagation;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getIsolation() {
		return isolation;
	}
	public void setIsolation(String isolation) {
		this.isolation = isolation;
	}
	public String getRollbackFor() {
		return rollbackFor;
	}
	public void setRollbackFor(String rollbackFor) {
		this.rollbackFor = rollbackFor;
	}
	public String getNoRollbackFor() {
		return noRollbackFor;
	}
	public void setNoRollbackFor(String noRollbackFor) {
		this.noRollbackFor = noRollbackFor;
	}
}
