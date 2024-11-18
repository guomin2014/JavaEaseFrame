package com.gm.javaeaseframe.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//在应用程序刚开始启动的时候,Spring容器都还没有初始化,用spring.factories方式将获取到更多的事件
//@Component
public class ApplicationListenerTest implements ApplicationListener<ApplicationEvent> 
{

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
//		logger.debug("**************event***************" + event);
	}

}
