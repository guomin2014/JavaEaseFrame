package com.gm.javaeaseframe.core.listener;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import com.gm.javaeaseframe.core.context.service.IApplicationService;
import com.gm.javaeaseframe.core.context.service.ICRUDCacheService;
import com.gm.javaeaseframe.core.listener.event.ContextPreparedEvent;

/**
 * 应用环境准备好后，可以先加载缓存等信息
 * 
 * @author GM
 * @date 2023年10月26日
 */
//@Component
public class ApplicationStartingListener implements ApplicationListener<ContextPreparedEvent>, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean initCacheDataSucc = false;
    private boolean startServiceSucc = false;
    
    private final ReentrantLock lock = new ReentrantLock();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextPreparedEvent event) {
        this.initCacheDatas(this.applicationContext);
        this.startService(this.applicationContext);
    }
    
    @SuppressWarnings("rawtypes")
    private void initCacheDatas(ApplicationContext context) {
        if (!initCacheDataSucc) {
            lock.lock();
            try {
                if (!initCacheDataSucc) {
                    logger.info("Start loading system cache...");
                    initCacheDataSucc = true;
                    Map<String, ICRUDCacheService> map = context.getBeansOfType(ICRUDCacheService.class);
                    for (ICRUDCacheService iCRUDCacheService : map.values()) {
                        try
                        {
                            iCRUDCacheService.loadCache();
                        }
                        catch(Exception e)
                        {
                            logger.error("loading cache error-->" + iCRUDCacheService, e);
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    private void startService(ApplicationContext context) {
        if (!startServiceSucc) {
            lock.lock();
            try {
                if (!startServiceSucc) {
                    startServiceSucc = true;
                    logger.info("Start the system front-end services...");
                    try {
                        Map<String, IApplicationService> map = context.getBeansOfType(IApplicationService.class);
                        if (map != null) {
                            for (Map.Entry<String, IApplicationService> entry : map.entrySet()) {
                                String name = entry.getKey();
                                IApplicationService service = entry.getValue();
                                if (service != null) {
                                    try {
                                        service.start();
                                    } catch (Throwable e) {
                                        logger.error("start service error:-->" + name, e);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {}
                }
            } finally {
                lock.unlock();
            }
        }
    }

}
