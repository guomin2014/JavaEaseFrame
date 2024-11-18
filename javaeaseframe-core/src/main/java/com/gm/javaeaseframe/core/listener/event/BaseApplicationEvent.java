package com.gm.javaeaseframe.core.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件基类
 * 
 * @author GM
 * @date 2023年10月25日
 */
@SuppressWarnings("serial")
public class BaseApplicationEvent extends ApplicationEvent {

	public BaseApplicationEvent(Object source) {
		super(source);
	}

}
