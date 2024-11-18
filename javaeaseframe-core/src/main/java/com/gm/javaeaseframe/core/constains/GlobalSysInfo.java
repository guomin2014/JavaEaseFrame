package com.gm.javaeaseframe.core.constains;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.gm.javaeaseframe.core.config.BasePropertyPlaceholderConfigurer;

/**
 * 通用信息类
 * 
 * @author GuoMin
 */
public class GlobalSysInfo {

	private static Log logger = LogFactory.getLog(GlobalSysInfo.class);
    /** WEB根路径 */
    public static String realRootPath;
    /** WEB相对目录：request.getContextPath() */
    public static String contextPath;
    /** spring上下文 */
    public static ApplicationContext context;
    /** 是否已经完成了启动 */
    public static boolean isCompleteStarted = false;
    /** 平台编码，在启动时初始化 */
    public static int platformCode = 0;
	
    public static Object getBeanByName(String name) {
        if (context != null) {
            try {
                return context.getBean(name);
            } catch (Exception e) {
            	logger.debug("获取对象异常-->" + name + "-->" + e.getMessage());
            }
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context != null) {
            try {
                return context.getBean(clazz);
            } catch (Exception e) {
            	logger.debug("获取对象异常-->" + clazz + "-->" + e.getMessage());
            }
        }
        return null;
    }
    public static <T> T getBean(String className) {
        if (context != null) {
            try {
                Class<T> clazz = (Class<T>)context.getType(className);
                return context.getBean(className, clazz);
            } catch (Exception e) {
                logger.debug("获取对象异常-->" + className + "-->" + e.getMessage());
            }
        }
        return null;
    }

    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        if (context != null) {
            try {
                return context.getBeansOfType(clazz);
            } catch (Exception e) {
            	logger.debug("获取对象异常-->" + clazz + "-->" + e.getMessage());
            }
        }
        return new HashMap<String, T>();
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T initBean(Class<T> clazz) {
        if (context != null) {
            try {
            	if(!context.containsBean(clazz.getName()))
            	{
//            		context.getAutowireCapableBeanFactory().autowire(clazz, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            		org.springframework.beans.factory.support.DefaultListableBeanFactory factory = 
            				(org.springframework.beans.factory.support.DefaultListableBeanFactory)context.getAutowireCapableBeanFactory();
            		T bean = (T)factory.createBean(clazz, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            		factory.registerSingleton(clazz.getName(), bean);
//            		return context.getAutowireCapableBeanFactory().createBean(clazz);
            		return bean;
            	}
            	else
            	{
            		return context.getBean(clazz);
            	}
            } catch (Exception e) {
            	logger.error("初始化Bean异常-->" + clazz, e);
            }
        }
        return null;
    }
    /**
     * 自动装载Bean属性
     * @param bean
     */
    public static void autowireBean(Object bean)
    {
    	if(bean == null)
    	{
    		return;
    	}
    	try
    	{
    	context.getAutowireCapableBeanFactory().autowireBeanProperties(bean, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    	}
    	catch(Exception e){
    		logger.error("自动装载Bean属性异常-->" + bean + "-->" + e.getMessage());
    	}
    }
    
    public static String getPropertyValue(String key)
    {
    	if(context.getEnvironment().containsProperty(key))
    	{
    		return context.getEnvironment().getProperty(key);
    	}
    	else
    	{
    		return BasePropertyPlaceholderConfigurer.getContextProperty(key);
    	}
    }
    
}
