package com.gm.javaeaseframe.core.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.gm.javaeaseframe.core.context.service.IApplicationService;
import com.gm.javaeaseframe.core.context.service.IApplicationStartedService;

//@Component
public class ApplicationCloseListener implements ApplicationListener<ContextClosedEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private boolean isStop = false;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
//        logger.info("application Stopping...");
        synchronized (logger) {
            if (!isStop) {
                logger.info("\n\n-------------------------------------------System shutting down-------------------------------------------");
                try {
                    logger.info("Start shutting down system backend services...");
                    // 退出程序后置服务的线程
                    Map<String, IApplicationStartedService> startAfterMap = event.getApplicationContext().getBeansOfType(IApplicationStartedService.class);
                    if (startAfterMap != null && !startAfterMap.isEmpty()) {
                        List<IApplicationStartedService> list = new ArrayList<>(startAfterMap.values());
                        Collections.sort(list, new Comparator<IApplicationStartedService>() {
                            @Override
                            public int compare(IApplicationStartedService o1, IApplicationStartedService o2) {
                                if (o1.getOrder() == o2.getOrder()) {
                                    return 0;
                                } else {
                                    return o1.getOrder() < o2.getOrder() ? 1 : -1;//从大到小倒序执行
                                }
                        }});
                        for (IApplicationStartedService service : list) {
                            if (service != null) {
                                try {
                                    service.stop();
                                } catch (Throwable e) {
                                    logger.error("stop service error --> " + service.getClass().getName(), e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    
                }
                logger.info("Start shutting down system front-end services...");
                try {
                    // 退出服务的线程
                    Map<String, IApplicationService> map = event.getApplicationContext().getBeansOfType(IApplicationService.class);
                    if (map != null) {
                        for (Map.Entry<String, IApplicationService> entry : map.entrySet()) {
                            String name = entry.getKey();
                            IApplicationService service = entry.getValue();
                            if (service != null) {
                                try {
                                    service.stop();
                                } catch (Throwable e) {
                                    logger.error("stop service error --> " + name, e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    
                }
                
                isStop = true;
            }
        }
    }

}
