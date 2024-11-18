package com.gm.javaeaseframe.core.context.service;

public interface ITask {

	/**
	 * 获取任务关键字
	 * @return
	 */
    public String getTaskKey();
    /**
     * 获取任务执行参数
     * @return
     */
    public String getExcuteParam();
    /**
     * 获取任务执行策略
     * @return
     */
    public Integer getExcuteStrategy();
    /**
     * 获取任务执行时间
     * @return
     */
    public Integer getExcuteDate();
}
