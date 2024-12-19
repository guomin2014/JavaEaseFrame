package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(prefix = PlatformConstants.PLATFORM_CONFIG_PREFIX + ".knife4j")
public class CustomKnife4jProperties extends Knife4jProperties{

}
