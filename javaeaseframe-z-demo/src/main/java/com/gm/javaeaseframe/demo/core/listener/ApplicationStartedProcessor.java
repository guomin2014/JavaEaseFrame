package com.gm.javaeaseframe.demo.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gm.javaeaseframe.core.context.service.IApplicationStartedService;

@Component
public class ApplicationStartedProcessor implements IApplicationStartedService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void start() {
		logger.info("开始启动测试服务...");
	}

	@Override
	public void stop() {
		logger.info("开始停止测试服务...");
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
