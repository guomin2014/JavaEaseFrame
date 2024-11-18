package com.gm.javaeaseframe.core.listener.event;

/**
 * 拦截器注册事件
 * 
 * @author GM
 * @date 2023年10月25日
 */
@SuppressWarnings("serial")
public class InterceptorRegistryEvent extends ContextPreparedEvent{

	public InterceptorRegistryEvent(Object source) {
		super(source);
	}

}
