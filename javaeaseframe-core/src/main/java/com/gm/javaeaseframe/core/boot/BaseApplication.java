package com.gm.javaeaseframe.core.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.gm.javaeaseframe.core.context.service.ICacheService;
import com.gm.javaeaseframe.core.context.service.ILogService;
import com.gm.javaeaseframe.core.context.service.impl.FileLogServiceImpl;
import com.gm.javaeaseframe.core.context.service.impl.LocalCacheServiceImpl;

/**
 * 启动类的基础类
 * 
 * @author	GM
 * @date	2020年7月8日
 */
public class BaseApplication {
    
    @Bean
    @ConditionalOnMissingBean({ICacheService.class})
    public ICacheService cacheService() {
        return new LocalCacheServiceImpl();
    }
    @Bean
    @ConditionalOnMissingBean({ILogService.class})
    public ILogService logService() {
    	return FileLogServiceImpl.getInstance();
    }
    
}
