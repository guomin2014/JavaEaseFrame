package com.gm.javaeaseframe.core.listener.event;

/**
 * 环境已准备好事件
 * 
 * @author GM
 * @date 2023年10月26日
 */
@SuppressWarnings("serial")
public class ContextPreparedEvent extends BaseApplicationEvent {

	public ContextPreparedEvent(Object source) {
		super(source);
	}

}
