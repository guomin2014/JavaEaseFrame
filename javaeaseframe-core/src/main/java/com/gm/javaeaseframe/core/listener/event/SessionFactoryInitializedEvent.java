package com.gm.javaeaseframe.core.listener.event;

/**
 * SessionFactory初始化完成事件
 * 
 * @author GM
 * @date 2023年10月25日
 */
@SuppressWarnings("serial")
public class SessionFactoryInitializedEvent extends ContextPreparedEvent{

	public SessionFactoryInitializedEvent(Object source) {
		super(source);
	}

}
