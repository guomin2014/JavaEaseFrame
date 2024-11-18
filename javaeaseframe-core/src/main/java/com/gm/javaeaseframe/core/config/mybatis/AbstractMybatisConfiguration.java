package com.gm.javaeaseframe.core.config.mybatis;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.gm.javaeaseframe.core.listener.event.SessionFactoryInitializedEvent;
import com.gm.javaeaseframe.core.thirty.mybatis.CustomPagePlugin;


/**
 * mybatis基础配置
 * 详见：https://github.com/pagehelper/Mybatis-PageHelper
 * @author	GM
 * @date	2020年7月8日
 */
public abstract class AbstractMybatisConfiguration {
    
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String ROOT_PATH_SPLIT = ",";

	@Autowired
	protected DataSource dataSource;
	
	@Autowired(required=false)
    private ApplicationContext applicationContext;

	private final String PATH_SEPARATOR = "/";

	// 提供SqlSeesion
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() {
		try {
		    logger.info("开始初始化SqlSessionFactory...");
			// 解决myBatis下 不能从嵌套jar文件中读取class的问题
			VFS.addImplClass(SpringBootVFS.class);
			SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
			sessionFactoryBean.setDataSource(dataSource);
			// 读取配置
			sessionFactoryBean.setTypeAliasesPackage(parseTypeAliasesPackage());
			// 读取SQL的配置文件(xml)
			Resource[] resources = new PathMatchingResourcePatternResolver().getResources(this.getSqlMappers());
			sessionFactoryBean.setMapperLocations(resources);
			//设置mybatis的配置（分页、缓存等）
			String configLocation = this.getMybatisConfig();
			if (StringUtils.isNotEmpty(configLocation)) {
			    logger.info("MyBatis运用配置文件-->" + configLocation);
    			Resource[] configLocations = new PathMatchingResourcePatternResolver().getResources(configLocation);
    			if (configLocations != null && configLocations.length > 0) {
    				sessionFactoryBean.setConfigLocation(configLocations[0]);
    			}
			} else {
			    logger.info("MyBatis使用默认配置");
			    //添加基础配置
//			    Properties properties = new Properties();
//			    properties.put("cacheEnabled", false);
//			    properties.put("lazyLoadingEnabled", false);
//			    properties.put("multipleResultSetsEnabled", true);
//			    properties.put("useColumnLabel", true);
//			    properties.put("useGeneratedKeys", false);
//			    properties.put("defaultExecutorType", "REUSE");
//			    sessionFactoryBean.setConfigurationProperties(properties);
			    Configuration configuration = new Configuration();
			    configuration.setCacheEnabled(false);
			    configuration.setLazyLoadingEnabled(false);
			    configuration.setMultipleResultSetsEnabled(true);
			    configuration.setUseColumnLabel(true);
			    configuration.setUseGeneratedKeys(false);
			    configuration.setDefaultExecutorType(ExecutorType.REUSE);
			    sessionFactoryBean.setConfiguration(configuration);
			    //添加插件
			    CustomPagePlugin pagePlugin = new CustomPagePlugin();
			    Properties pageProp = new Properties();
			    pageProp.put("dialect", "MySql");
			    pagePlugin.setProperties(pageProp);
			    sessionFactoryBean.setPlugins(new Interceptor[]{pagePlugin});
			}
			return sessionFactoryBean.getObject();
		} catch (IOException e) {
			logger.error("mybatis resolver mapper*xml is error", e);
			return null;
		} catch (Exception e) {
			logger.error("mybatis sqlSessionFactoryBean create error", e);
			return null;
		} finally {
			if (applicationContext != null) {
				applicationContext.publishEvent(new SessionFactoryInitializedEvent("Session factory initialized"));
			}
		}
	}

	private String parseTypeAliasesPackage() {
	    String typeAliasesPackage = this.getTypeAliasesPackage();
	    //添加框架的model对象
	    if (StringUtils.isEmpty(typeAliasesPackage)) {
	    	typeAliasesPackage = "com.gm.javaeaseframe.core.context.model";
	    } else {
	    	typeAliasesPackage += ",com.gm.javaeaseframe.core.context.model";
	    }
        if (StringUtils.isEmpty(typeAliasesPackage)) {
            return "";
        }
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		StringBuffer typeAliasesPackageStringBuffer = new StringBuffer();
		try {
			for (String location : typeAliasesPackage.split(ROOT_PATH_SPLIT)) {
				if (StringUtils.isEmpty(location)) {
					continue;
				}
				if (location.contains("*")) {
					location = "classpath*:" + location.trim().replace(".", PATH_SEPARATOR);
					location = getResources(resolver, location);
				}
				if (location.endsWith(PATH_SEPARATOR)) {
					location = location.substring(0, location.length() - 1);
				}
				typeAliasesPackageStringBuffer.append(location + ROOT_PATH_SPLIT);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (typeAliasesPackageStringBuffer.length() == 0) {
			throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误！请检查applicationContext.xml@sqlSessionFactory配置！");
		}
		String allTypeAliasesPackage = typeAliasesPackageStringBuffer.toString().replace(PATH_SEPARATOR, ".");
		logger.info("MyBatis扫描包路径：" + allTypeAliasesPackage);
		return allTypeAliasesPackage;
	}

	private String getResources(ResourcePatternResolver resolver, String location) throws IOException {
	    String rootPath = getTypeAliasesRootPackage();
        if (StringUtils.isNotEmpty(rootPath)) {
            rootPath = rootPath.trim().replace(".", PATH_SEPARATOR);
        }
        String packPrefix = rootPath;
        if (StringUtils.isEmpty(packPrefix)) {
            String packPath = null;
            if (location.startsWith("classpath*:")) {
                packPath = location.substring("classpath*:".length());
            } else {
                packPath = location;
            }
            packPath = packPath.replaceAll("\\\\", PATH_SEPARATOR);// 将所有反斜杠（\）替换成正斜杠（/）
            int index = packPath.indexOf(PATH_SEPARATOR);
            if (index != -1) {
                packPrefix = packPath.substring(0, index-1);
            } else {
                packPrefix = packPath;
            }
        }
		StringBuffer resourcePathStringBuffer = new StringBuffer();
		for (Resource resource : resolver.getResources(location)) {
			if (resource == null || resource.getURL() == null) {
				continue;
			}
			String path = resource == null ? "" : resource.getURL().getPath();
			path = URLDecoder.decode(path, "UTF-8");// 对URL进行解码
			path = path.replaceAll("\\\\", PATH_SEPARATOR);// 将所有反斜杠（\）替换成正斜杠（/）
			if (StringUtils.isEmpty(path) || path.indexOf(packPrefix) == -1) {
				continue;
			}
			if (path.endsWith(PATH_SEPARATOR)) {
				path = path.substring(0, path.length() - 1);
			}
			resourcePathStringBuffer.append(path.substring(path.lastIndexOf(rootPath))).append(ROOT_PATH_SPLIT);
		}
		return resourcePathStringBuffer.toString();
	}
	
	/**
	 * 获取所有mybatis的SQL映射文件（xml）
	 * 如：'classpath*:sqlmap\/**\/*.xml'
	 * @return 默认为：classpath*:sqlmap\/**\/*.xml
	 */
	public String getSqlMappers() {
	    return "classpath*:sqlmap\\/**\\/*.xml";
	}
	/**
	 * 获取Mybatis的配置文件（比如：分页，是否开启缓存等参数配置）
	 * 该文件曾用名：mybatis-sqlmap-config.xml
	 * 曾使用路径：classpath*:config/mybatis-sqlmap-config.xml
	 * @return 空或空串：将使用系统默认配置参数以及默认分页框架
	 */
	public String getMybatisConfig() {
	    return null;
	}
	/**
	 * 获取Mybatis的Sql映射文件中定义类型对应的实体包路径，多个路径使用逗号分隔
	 * 比如：com.gm.javaeaseframe.model,com.gm.javaeaseframe.order.**.model
	 * @return
	 */
	public abstract String getTypeAliasesPackage();
	/**
	 * 获取Mybatis的Sql映射文件中定义类型对应的实体包的顶层包路径，用于过滤包
	 * @return 默认为：com.gm.javaeaseframe
	 */
	public String getTypeAliasesRootPackage() {
	    return "com.gm.javaeaseframe";
	}
	

}
