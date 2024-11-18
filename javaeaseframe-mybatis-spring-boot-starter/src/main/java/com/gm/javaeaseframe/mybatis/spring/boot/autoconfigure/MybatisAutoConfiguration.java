package com.gm.javaeaseframe.mybatis.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.gm.javaeaseframe.core.config.mybatis.AbstractMybatisConfiguration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@ConfigurationProperties("javaeaseframe.mybatis")
public class MybatisAutoConfiguration extends AbstractMybatisConfiguration {

    // 配置包根目录
    private String rootPath = "com.gm.javaeaseframe";
    
    // 配置类型别名
    private String typeAliasesPackage = "com.gm.javaeaseframe.**.model";

    // 配置mapper的扫描，找到所有的mapper.xml映射文件
    private String mapperLocations = "classpath*:sqlmap/**/*.xml";
    
    // 加载全局的配置文件
    private String configLocation;//"classpath*:config/mybatis-sqlmap-config.xml";
    
    @Override
    public String getSqlMappers() {
        return this.mapperLocations;
    }

    @Override
    public String getTypeAliasesPackage() {
        return this.typeAliasesPackage;
    }

    @Override
    public String getMybatisConfig() {
        return this.configLocation;
    }

	@Override
	public String getTypeAliasesRootPackage() {
		return this.rootPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}
}