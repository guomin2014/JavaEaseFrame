package com.gm.javaeaseframe.core.listener;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gm.javaeaseframe.core.context.dynamic.DynamicClassLoader;

//@Component
public class ApplicationRefreshedListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String EXT_LIB_FILE_PATH = "application.lib.ext.path";

    private boolean dynamicClassLoaderSucc = false;
    
    private final ReentrantLock lock = new ReentrantLock();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.startDynamicClassLoader(event);
    }
    
    private void startDynamicClassLoader(ContextRefreshedEvent event) {
        if (!dynamicClassLoaderSucc) {
            lock.lock();
            try {
                if (!dynamicClassLoaderSucc) {
                    String extLibFilePath = event.getApplicationContext().getEnvironment().getProperty(EXT_LIB_FILE_PATH);
                    if (extLibFilePath != null && extLibFilePath.trim().length() > 0) {
                        logger.info("开启动态加载扩展包功能-->" + extLibFilePath);
                        dynamicClassLoaderSucc = true;
                        DynamicClassLoader loader = DynamicClassLoader.getInstance();
                        loader.start(this.applicationContext);
                        loader.initializeExtFile(extLibFilePath);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
