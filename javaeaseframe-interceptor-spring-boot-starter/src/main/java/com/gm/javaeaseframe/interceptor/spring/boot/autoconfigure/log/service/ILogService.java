package com.gm.javaeaseframe.interceptor.spring.boot.autoconfigure.log.service;

import java.util.Map;

import com.gm.javaeaseframe.core.context.service.IService;

/**
 * 日志服务接口(将框架的操作日志对外提供的主入口)
 * @author	GuoMin
 * @date	2016年5月20日
 */
public interface ILogService extends IService
{
	/**
	 * 
	 * @param format			日志输出格式
	 * @param variableValueMap	日志输出的变量值的集合
	 */
	void doHandlerLog(String format, Map<String, String> variableValueMap);
}
