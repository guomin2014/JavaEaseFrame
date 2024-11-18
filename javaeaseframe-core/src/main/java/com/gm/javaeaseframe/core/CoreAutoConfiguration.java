package com.gm.javaeaseframe.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CoreAutoConfiguration {

	@ComponentScan(basePackages = {
            "com.gm.javaeaseframe.core.listener"
    })
    public class CoreListenerAutoConfiguration {
		
	}
}
