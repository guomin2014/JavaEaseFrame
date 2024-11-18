package com.gm.javaeaseframe.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.StringValueResolver;

public class BasePropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer
{
	private static Map<String, Object> ctxPropertiesMap = new HashMap<String, Object>();
	
	private static boolean isInit = false;

//	@Override
//	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException
//	{
//		super.processProperties(beanFactoryToProcess, props);
//		for (Object key : props.keySet())
//		{
//			String keyStr = key.toString();
//			String value = props.getProperty(keyStr);
//			ctxPropertiesMap.put(keyStr, value);
//		}
//		isInit = true;
//	}
	

	@Override
	protected void doProcessProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			StringValueResolver valueResolver) {
		super.doProcessProperties(beanFactoryToProcess, valueResolver);
		//TODO 解析属性
	}


	@SuppressWarnings("unchecked")
	public static <T> T getContextProperty(String name)
	{
		if(!isInit)
		{
			throw new RuntimeException("未配置初始化");
		}
		return (T)ctxPropertiesMap.get(name);
	}
}
