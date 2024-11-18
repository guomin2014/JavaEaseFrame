package com.gm.javaeaseframe.core.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gm.javaeaseframe.core.context.service.IApplicationStartedService;

/**
 * 
 * 程序后置服务，在Spring容器完成初始化，并且所有Bean都完全加载
 * 
 * @author GM
 * @date 2023年9月15日
 */
@Component
public class ApplicationStartedListener implements ApplicationRunner, ApplicationContextAware {
	private Log logger = LogFactory.getLog(getClass());

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.start();
	}

	public void start() {
		logger.info("开始启动系统后置服务...");
		try {
			Map<String, IApplicationStartedService> serviceMap = this.applicationContext
					.getBeansOfType(IApplicationStartedService.class);
			if (serviceMap != null && serviceMap.size() > 0) {
				List<IApplicationStartedService> serviceList = new ArrayList<>(serviceMap.values());
				Collections.sort(serviceList, new Comparator<IApplicationStartedService>() {
					@Override
					public int compare(IApplicationStartedService o1, IApplicationStartedService o2) {
						if (o1.getOrder() == o2.getOrder()) {
							return 0;
						} else {
							return o1.getOrder() < o2.getOrder() ? -1 : 1;// 从小到大顺序执行
						}
					}
				});
				for (IApplicationStartedService server : serviceList) {
					try {
						server.start();
					} catch (Exception e) {
						logger.error("启动系统后置服务异常-->" + server.getClass().getName(), e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取系统后置服务异常", e);
		}
	}

}
