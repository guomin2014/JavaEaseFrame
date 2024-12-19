package com.gm.javaeaseframe.knife4j.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.xiaoymin.knife4j.spring.model.docket.Knife4jAuthInfoProperties;
import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(prefix = PlatformConstants.PLATFORM_CONFIG_PREFIX + ".knife4j.openapi.basic-auth")
public class CustomKnife4jAuthInfoProperties extends Knife4jAuthInfoProperties {

}
