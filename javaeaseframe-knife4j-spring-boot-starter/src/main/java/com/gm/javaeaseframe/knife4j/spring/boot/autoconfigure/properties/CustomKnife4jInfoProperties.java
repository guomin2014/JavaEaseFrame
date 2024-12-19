package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jInfoProperties;
import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(prefix = PlatformConstants.PLATFORM_CONFIG_PREFIX + ".knife4j.openapi")
public class CustomKnife4jInfoProperties extends Knife4jInfoProperties {

}
