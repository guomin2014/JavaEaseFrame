package com.gm.javaeaseframe.core.context.dynamic;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;


public class DynamicClassLoader
{
	private Log logger = LogFactory.getLog(getClass());
	
	private static DynamicClassLoader instance = new DynamicClassLoader();
	
	private WatchEventHandler watchEventHandler;
	
	private ApplicationContext applicationContext;
	/** key：jar文件名称，value：ClassLoader */
	private Map<String, ClassLoader> classLoaderMap;
	/** key：jar文件名称，value：Class名称集合 */
	private Map<String, Set<String>> classNameMap;
	/** 服务是否启动 */
	private boolean isRun = false;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private DynamicClassLoader() {
		classLoaderMap = new HashMap<>();
		classNameMap = new HashMap<>();
		watchEventHandler = new WatchEventHandler(new IWatchEventCallback(){
			@Override
			public void callback(String fileName, String filePath, WatchEventKind kind)
			{
				reInitializeExtFile(fileName, filePath, kind);
			}});
	}
	
	public static DynamicClassLoader getInstance() {
		return instance;
	}
	/**
	 * 启动监控
	 */
	public void start(ApplicationContext applicationContext) {
	    logger.info("启动动态加载扩展文件服务...");
	    this.applicationContext = applicationContext;
	    if (watchEventHandler != null) {
	        watchEventHandler.start();
	    }
	    isRun = true;
	}
	
    public void stop() {
        logger.info("停止动态加载扩展文件服务...");
        isRun = false;
        if (watchEventHandler != null) {
            watchEventHandler.stop();
        }
        classLoaderMap.clear();
        classNameMap.clear();
    }

	/**
	 * 加载文件（jar、zip）
	 * 只加载一级深度的目录
	 * @param filePath	文件路径
	 */
	public void initializeExtFile(String filePath)
	{
	    if (!isRun) {
	        throw new RuntimeException("动态加载扩展文件服务未启动");
	    }
		if(filePath == null || filePath.trim().length() == 0)
		{
			return;
		}
		logger.info("开始加载扩展文件-->" + filePath);
		boolean needLoadClass = true;
		// 系统类库路径
		File libPath = new File(filePath);
		if(!libPath.exists())
		{
			needLoadClass = false;
			boolean result = libPath.mkdirs();
			logger.warn("动态创建扩展文件路径" + (result ? "成功" : "失败") + "-->" + filePath);
			if(!result)
			{
				logger.warn("动态加载扩展类异常，未知的扩展类路径-->" + filePath);
				return;
			}
		}
		if(!libPath.isDirectory())
		{
			logger.warn("动态加载扩展类异常，扩展类路径只能是文件夹-->" + filePath);
			return;
		}
		if(needLoadClass)
		{
			lock.lock();
			try
			{
				File[] files = libPath.listFiles(new FilenameFilter()
				{
					@Override
					public boolean accept(File dir, String name)
					{
						return name.endsWith(".jar") || name.endsWith(".zip");
					}
				});
				for(File subFile : files)
				{
					initializeExtFile(subFile);
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		//注册目录监控
		watchEventHandler.registDir(filePath);
	}
	/**
	 * 释放资源
	 * @param filePath
	 */
	public void releaseExtFile(String filePath)
	{
		if(filePath == null || filePath.trim().length() == 0)
		{
			return;
		}
		// 系统类库路径  
		File libPath = new File(filePath);
		if(!libPath.exists())
		{
			logger.warn("动态释放扩展类异常，未知的扩展类路径-->" + filePath);
			return;
		}
		lock.lock();
		try
		{
			if(libPath.isDirectory())
			{
				File[] files = libPath.listFiles(new FilenameFilter()
				{
					@Override
					public boolean accept(File dir, String name)
					{
						return name.endsWith(".jar") || name.endsWith(".zip");
					}
				});
				for(File subFile : files)
				{
					this.releaseExtFile(subFile);
				}
			}
			else if(libPath.isFile())
			{
				this.releaseExtFile(libPath);
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	private void releaseExtFileByName(String fileName)
	{
		classNameMap.remove(fileName);
		ClassLoader classLoader = classLoaderMap.remove(fileName);
		if(classLoader != null && classLoader instanceof URLClassLoader)
		{
			try
			{
				logger.info("动态释放扩展类-->" + fileName);
				((URLClassLoader)classLoader).close();
			}
			catch(Throwable e)
			{
				logger.error("动态释放扩展类异常-->" + fileName, e);
			}
		}
	}
	
	private void releaseExtFile(File file)
	{
		if(file == null || !file.exists())
		{
			return;
		}
		String name = file.getName();
		classNameMap.remove(name);
		ClassLoader classLoader = classLoaderMap.remove(name);
		if(classLoader != null && classLoader instanceof URLClassLoader)
		{
			try
			{
				logger.info("动态释放扩展类-->" + name);
				((URLClassLoader)classLoader).close();
			}
			catch(Throwable e)
			{
				logger.error("动态释放扩展类异常-->" + file.getAbsolutePath(), e);
			}
		}
	}
	
	private void initializeExtFile(File file)
	{
		if(file == null || !file.exists())
		{
			return;
		}
		try
		{
			URL url = file.toURI().toURL();
			ClassLoader classLoader = null;
			if(applicationContext != null)
			{
				classLoader = applicationContext.getClassLoader();
			}
			if(classLoader != null)
			{
				classLoader = URLClassLoader.newInstance(new URL[]{url}, classLoader);
			}
			else
			{
				classLoader = new URLClassLoader(new URL[]{url});
			}
			 // 获取系统类加载器，可以直接将类加载到系统中，但不能单独销毁
//	        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			classLoaderMap.put(file.getName(), classLoader);
	        logger.info("加载扩展文件-->" + file.getAbsolutePath()); 
	        
	        Set<String> classNames = new HashSet<>();
	        JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(file);
				Enumeration<JarEntry> jars = jarFile.entries();
				while(jars.hasMoreElements())
				{
					JarEntry entry = jars.nextElement();
					if(entry.isDirectory())
					{
						continue;
					}
					String name = entry.getName();
					if(name.endsWith(".class") && name.indexOf("$") == -1)
					{
						name = name.substring(0, name.length() - 6);
						name = StringUtils.replaceChars(name, '\\', '/');
						name = name.replace("/", ".");
						classNames.add(name);
						logger.info("loader class [" + name + "] from [" + file.getName() + "]");
					}
				}
			}
			catch(Throwable e)
			{
				logger.error("加载扩展文件异常-->" + file.getAbsolutePath(), e);
			}
			finally
			{
				if(jarFile != null)
				{
					try
					{
						jarFile.close();
					}catch(Exception e){}
				}
			}
			classNameMap.put(file.getName(), classNames);
			for(String className : classNames)
			{
				this.reInitBean(className);
			}
		}
		catch(Throwable e)
		{
			logger.error("加载扩展文件异常-->" + file.getAbsolutePath(), e);
		}
	}
	
	private void reInitializeExtFile(String fileName, String filePath, WatchEventKind kind)
	{
		if(fileName.endsWith(".jar") || fileName.endsWith(".zip"))
		{
			logger.info("文件变更【" + kind + "】-->" + filePath);
			lock.lock();
			try
			{
				this.releaseExtFileByName(fileName);
				File file = new File(filePath);
				switch(kind)
				{
					case ENTRY_CREATE:
						logger.info("发现新的扩展文件-->" + filePath);
						initializeExtFile(file);
						break;
					case ENTRY_MODIFY:
						logger.info("扩展文件被修改-->" + filePath);
						initializeExtFile(file);
						break;
					case ENTRY_DELETE:
						logger.info("扩展文件被删除-->" + filePath);
						break;
				}
			}
			finally
			{
				lock.unlock();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> T getSingletonBean(String className, Class<T> clazz) throws Exception
	{
		if (applicationContext != null) {
        	if(!applicationContext.containsBean(className))
        	{
//            		context.getAutowireCapableBeanFactory().autowire(clazz, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        		org.springframework.beans.factory.support.DefaultListableBeanFactory factory = 
        				(org.springframework.beans.factory.support.DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        		Class<?> clazzTmp = this.loadClass(className);
        		T bean = (T)factory.createBean(clazzTmp, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        		factory.registerSingleton(className, bean);
//            		return context.getAutowireCapableBeanFactory().createBean(clazz);
        		return bean;
        	}
        	else
        	{
        		return applicationContext.getBean(className, clazz);
        	}
        }
        return null;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> T getBean(String className, Class<T> clazz) throws Exception
	{
		if (applicationContext != null) 
		{
			Class<?> clazzTmp = this.loadClass(className);
    		return (T)applicationContext.getAutowireCapableBeanFactory().createBean(clazzTmp);
		}
		return null;
	}
	
	public synchronized void initBean(String className)
	{
		if (applicationContext != null) 
		{
			try
			{
	        	if(!applicationContext.containsBean(className))
	        	{
	        		org.springframework.beans.factory.support.DefaultListableBeanFactory factory = 
	        				(org.springframework.beans.factory.support.DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
	        		Class<?> clazzTmp = this.loadClass(className);
	        		Object bean = factory.createBean(clazzTmp, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
	        		factory.registerSingleton(className, bean);
	        		logger.info("初始化Bean [" + className + "]");
	        	}
			}
			catch(Throwable e)
			{
				logger.error("初始化Bean异常 [" + className + "]", e);
			}
        }
	}
	
	private synchronized void reInitBean(String className)
	{
		if (applicationContext != null) 
		{
			try
			{
				Class<?> clazzTmp = this.loadClass(className);
				if(Dynamic.class.isAssignableFrom(clazzTmp))
				{
					org.springframework.beans.factory.support.DefaultListableBeanFactory factory = 
	        				(org.springframework.beans.factory.support.DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
					if(applicationContext.containsBean(className))
		        	{
						factory.destroySingleton(className);
		        	}
					Object bean = factory.createBean(clazzTmp, org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
	        		factory.registerSingleton(className, bean);
	        		logger.info("重新初始化Bean [" + className + "]");
				}
			}
			catch(Throwable e)
			{
				logger.error("重新初始化Bean异常 [" + className + "]", e);
			}
        }
	}
	
	public Class<?> loadClass(String className) throws ClassNotFoundException
	{
		lock.lock();
		try
		{
			for(Map.Entry<String, Set<String>> entry : classNameMap.entrySet())
			{
				if(entry.getValue().contains(className))
				{
					ClassLoader classLoader = classLoaderMap.get(entry.getKey());
					if(classLoader != null)
					{
						return classLoader.loadClass(className);
					}
				}
			}
			throw new ClassNotFoundException(className);
		}
		finally
		{
			lock.unlock();
		}
	}
	
}
